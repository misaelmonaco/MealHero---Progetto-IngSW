package it.unisannio.paymentmanagementservice.service;

import it.unisannio.paymentmanagementservice.entity.BankAccount;
import it.unisannio.paymentmanagementservice.entity.Customer;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Service;

@Service
public class ScoreService {

    public ScoreService() {}

    public static void userSetScore(double amount, int userId){
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

        int points = (int)amount/10;

        resource = target.path("score/" + points + "/" + customer.getId());

        try{
            Response response = resource.request().put(null);
        } catch (Exception e){
            System.err.println("Impossibile assegnare dei punti all'utente con id " + userId + "!\n" + e.getMessage());
        }
    }

    public static double checkAndCalculateDiscount(double amount, int userId){
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
            return 0f;
        }

        // Il massimo sconto ottenibile vale il 95% della somma totale
        // (questo perchè in caso di errore viene ritornato 0.0 e non può essere confuso con unosconto del 100%)
        double discountPercentage = customer.getScore() * 0.5;
        if(discountPercentage > 95.0) // Accade con 191 punti
            discountPercentage = 95.0;

        double discountAmount = amount * (discountPercentage / 100);

        double finalAmount = amount - discountAmount;
        if (finalAmount < amount * 0.05) {
            finalAmount = amount * 0.05;
        }

        resource = target.path("score/" + (-customer.getScore()) + "/" + customer.getId());
        try{
            Response response = resource.request().put(null);
        } catch (Exception e){
            System.err.println("Impossibile rimuovere tutti i punti dall'utente con id " + userId + "!\n" + e.getMessage());
            return 0f;
        }

        //Crea il conto bancario di MealHero
        target = client.target("http://localhost:8086/bankAccount/");
        resource = target.path(String.valueOf(3));
        BankAccount bankAccount = null;
        try{
            bankAccount = resource.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(BankAccount.class);
        } catch (Exception e){
            System.err.println("Impossibile recuperare il conto bancario di MealHero!");
            return 0f;
        }
        bankAccount.setBalance(bankAccount.getBalance() - discountAmount);

        resource = target.path("update/3");
        try{
            Response put = resource.request().put(Entity.entity(bankAccount, MediaType.APPLICATION_JSON));
        } catch (Exception e){
            System.err.println("Impossibile aggiornare il conto bancario di MealHero!");
            return 0f;
        }

        return finalAmount;
    }
}
