package it.unisannio.paymentmanagementservice.service;

import it.unisannio.paymentmanagementservice.model.entity.BankAccount;
import it.unisannio.paymentmanagementservice.model.Customer;
import it.unisannio.paymentmanagementservice.model.entity.BankAccountTransaction;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ScoreService {

    public ScoreService() {}

    public static void userSetScore(BigDecimal amount, int userId){
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8087/customer/");
        WebTarget resource = target.path(String.valueOf(userId));

        Customer customer = null;
        try{
            customer = resource.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Customer.class);
        } catch (Exception e){
            System.err.println("Impossibile recuperare l'utente con id " + userId + "!\n" + e.getMessage());
        }

        int points = amount.intValue()/10;

        resource = target.path("score/" + points + "/" + customer.getId());

        try{
            Response response = resource.request(MediaType.APPLICATION_JSON)
                    .put(Entity.entity(points, MediaType.APPLICATION_JSON));
        } catch (Exception e){
            System.err.println("Impossibile assegnare dei punti all'utente con id " + userId + "!\n" + e.getMessage());
        }
    }

    public static BigDecimal checkAndCalculateDiscount(BigDecimal amount, int userId, int restaurantPayeeId, String currency) throws Exception {

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8087/customer/");
        WebTarget resource = target.path(String.valueOf(userId));

        Customer customer = null;
        try{
            customer = resource.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Customer.class);
        } catch (Exception e){
            System.err.println("Impossibile recuperare l'utente con id " + userId + "!\n" + e.getMessage());
            return BigDecimal.ZERO;
        }

        if(customer.getScore() == 0)
            return BigDecimal.ZERO;

        // Il massimo sconto ottenibile vale il 95% della somma totale
        // (questo perchè in caso di errore viene ritornato 0.0 e non può essere confuso con unosconto del 100%)
        BigDecimal discountPercentage = BigDecimal.valueOf(0.5).multiply(new BigDecimal(customer.getScore()));
        if(discountPercentage.compareTo(BigDecimal.valueOf(95.0)) > 0) // Accade con 191 punti
            discountPercentage = new BigDecimal("95.0");

        BigDecimal discountAmount = amount.multiply(discountPercentage.divide(new BigDecimal("100")));

        BigDecimal finalAmount = amount.subtract(discountAmount);
        if (finalAmount.compareTo(amount.multiply(BigDecimal.valueOf(0.05))) < 0) {
            finalAmount = amount.multiply(BigDecimal.valueOf(0.05));
        }

        resource = target.path("score/subtract/" + (customer.getScore()) + "/" + customer.getId());
        try{
            Response response = resource.request().put(Entity.text(""));
        } catch (Exception e){
            System.err.println("Impossibile rimuovere i punti dall'utente con id " + userId + "!\n" + e.getMessage());
            return BigDecimal.ZERO;
        }

        //Crea il conto bancario di MealHero
        target = client.target("http://localhost:8086/bankAccount/");
        resource = target.path(String.valueOf(9));
        BankAccount bankAccount = null;
        try{
            bankAccount = resource.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(BankAccount.class);
        } catch (Exception e){
            System.err.println("Impossibile recuperare il conto bancario di MealHero!");
            return BigDecimal.ZERO;
        }
        bankAccount.setBalance(bankAccount.getBalance().subtract(discountAmount));

        resource = target.path("update/9");
        try{
            Response put = resource.request().put(Entity.entity(bankAccount, MediaType.APPLICATION_JSON));
        } catch (Exception e){
            System.err.println("Impossibile aggiornare il conto bancario di MealHero!");
            return BigDecimal.ZERO;
        }

        resource = client.target("http://localhost:8086/bankAccountTransaction/add");
        BankAccountTransaction transaction = new BankAccountTransaction(bankAccount.getId(), restaurantPayeeId, discountAmount, currency);
        Response response = resource.request().post(Entity.entity(transaction, MediaType.APPLICATION_JSON));
        if(response.getStatus() != 201){
            System.err.println("ATTENZIONE! È stato effettuato un pagamento ma non è stato possibile generare la corrispondente tansazione! \nUserId: " + userId + "\nRestaurantPayeeId: " + restaurantPayeeId + "\nAmount: " + discountAmount + "\nCurrency: " + currency);
        }

        return finalAmount;
    }
}
