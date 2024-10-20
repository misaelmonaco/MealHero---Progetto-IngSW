package it.unisannio.paymentmanagementservice.service;

import it.unisannio.paymentmanagementservice.model.Customer;
import it.unisannio.paymentmanagementservice.model.entity.*;
import it.unisannio.paymentmanagementservice.model.PaymentMethod;
import it.unisannio.paymentmanagementservice.model.PaymentType;
import it.unisannio.paymentmanagementservice.repository.*;
import jakarta.persistence.Column;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class PaymentDetailService {

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private PayPalAccountRepository paypalAccountRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserOrderDetailRepository userOrderDetailRepository;

    @Autowired
    private PayPalTransactionRepository payPalTransactionRepository;

    @Autowired
    private CardTransactionRepository cardTransactionRepository;
    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    // L'id del pagamento si prende dalla mail ricevuta nel momento di creazione del singolo PaymentDetail
    public ResponseEntity updatePaymentDetail(int id, String method){
        PaymentDetail detail = paymentDetailRepository.findById(id).orElse(null);
        if(detail == null)
            return ResponseEntity.notFound().build();

        detail.setPaymentMethod(PaymentMethod.valueOf(method));

        if(detail.getAmount() == 0f) //Siamo di fronte un PERSONALLY SPLIT Payment
            return updatePaymentDetailPersonallySplit(detail, detail.getUserId());

        paymentDetailRepository.save(detail);
        return ResponseEntity.ok().body("Metodo di pagamento selezionato, ora puoi procedere con la transazione!");
    }

    public ResponseEntity pay(int id, int employeeId) throws Exception {
        PaymentDetail detail = paymentDetailRepository.findById(id).orElse(null);
        if(detail == null)
            return ResponseEntity.notFound().build();

        ResponseEntity responseEntity;
        switch(detail.getPaymentMethod()){
            case CARD:
                responseEntity = payWithCard(detail);
                return responseEntity;

            case PAYPAL:
                responseEntity = payWithPayPal(detail);
                return responseEntity;

            case CASH:
                if(employeeId == 0)
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Non è stato specificato l'id del dipendente responsabile del pagamento");
                responseEntity = payWithCash(detail, employeeId);
                return responseEntity;

            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile effettuare il pagamento!");
        }
    }

    public PaymentDetail getById(int id){
        return paymentDetailRepository.findById(id).orElse(null);
    }

    public Iterable<PaymentDetail> getByUserId(int userId){
        return paymentDetailRepository.findByUserId(userId);
    }

    public Iterable<PaymentDetail> getByPaymentId(int paymentId){
        return paymentDetailRepository.findByPaymentId(paymentId);
    }

    public Iterable<PaymentDetail> getAllDetails() {
        return paymentDetailRepository.findAll();
    }

    /**
     *   METODI PRIVATI AD USO DEL PAYMENT DETAIL SERVICE
     */

    // Metodo update per PERSONALLY_SPLIT
    private ResponseEntity updatePaymentDetailPersonallySplit(PaymentDetail detail, int userId){
        float amount = calculateUserAmount(userId);
        if(amount == 0)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Non è stato possibile calcolare la somma da pagare!");
        detail.setAmount(amount);
        paymentDetailRepository.save(detail);
        return ResponseEntity.ok().body("Metodo di pagamento selezionato, ora puoi procedere con la transazione!");
    }

    @Transactional
    protected ResponseEntity payWithCard(PaymentDetail detail) throws Exception {

        CreditCard creditCard = creditCardRepository.findByUserId(detail.getUserId()).orElse(null);
        if(creditCard == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utente non ha una carta di credito associata! Associane una prima di effettuare il pagamento!");

        if(!creditCard.hasSufficientBalance(detail.getAmount()))
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("La carta di credito associata non ha abbastanza liquidità per effettuare il pagamento!");

        Payment payment = paymentRepository.findById(detail.getPaymentId()).orElse(null);
        if(payment == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Non è stato possibile trovare il pagamento associato!");

        BankAccount restaurantAccount = bankAccountRepository.findById(payment.getRestaurantPayeeId()).orElse(null);
        if(restaurantAccount == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Impossibile effettuare un pagamento a questo ristorante: conto bancario non trovato!");

        double discountedAmount = 0f;
        try{
            ScoreService.checkAndCalculateDiscount(detail.getAmount(), detail.getUserId(), payment.getRestaurantPayeeId(), payment.getCurrency().toString());
        } catch (Exception e){}
        if(discountedAmount == 0f){
            try{
                ScoreService.userSetScore(detail.getAmount(), detail.getUserId());
            } finally {
                creditCard.charge(detail.getAmount());
            }
        }
        else{
            try{
                ScoreService.userSetScore(discountedAmount, detail.getUserId());
                creditCard.charge(discountedAmount);
            } catch (Exception e){
                creditCard.charge(detail.getAmount());
            }
        }
        CardTransaction transaction;
        restaurantAccount.setBalance(restaurantAccount.getBalance() + detail.getAmount());
        if(discountedAmount == 0f) {
            transaction = new CardTransaction(detail.getAmount(), payment.getCurrency().toString(), detail.getUserId(), restaurantAccount.getRestaurantId(), payment.getPaymentType().toString(), creditCard.getCardNumber());
        } else {
            transaction = new CardTransaction(discountedAmount, payment.getCurrency().toString(), detail.getUserId(), restaurantAccount.getRestaurantId(), payment.getPaymentType().toString(), creditCard.getCardNumber());
        }
        bankAccountRepository.save(restaurantAccount);
        creditCardRepository.save(creditCard);
        cardTransactionRepository.save(transaction);
        return checkStatusAndSendEmail(detail);
    }

    @Transactional
    protected ResponseEntity payWithPayPal(PaymentDetail detail) throws Exception {
        PayPalAccount account = paypalAccountRepository.findByUserId(detail.getUserId()).orElse(null);
        if(account == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("L'utente non ha un account PayPal associato! Associane uno prima di effettuare il pagamento!");

        if(!account.hasSufficientBalance(detail.getAmount()))
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("L'account PayPal associato non ha abbastanza liquidità per effettuare il pagamento!");

        Payment payment = paymentRepository.findById(detail.getPaymentId()).orElse(null);
        if(payment == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Non è stato possibile trovare il pagamento associato!");

        BankAccount restaurantAccount = bankAccountRepository.findById(payment.getRestaurantPayeeId()).orElse(null);
        if(restaurantAccount == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Impossibile effettuare un pagamento a questo ristorante: conto bancario non trovato!");

        double discountedAmount = 0f;
        try{
            discountedAmount = ScoreService.checkAndCalculateDiscount(detail.getAmount(), detail.getUserId(), payment.getRestaurantPayeeId(), payment.getCurrency().toString());
        } catch (Exception e){}
        if(discountedAmount == 0f){
            try{
                ScoreService.userSetScore(detail.getAmount(), detail.getUserId());
            } finally {
                account.charge(detail.getAmount());
            }
        }
        else{
            try{
                ScoreService.userSetScore(discountedAmount, detail.getUserId());
                account.charge(discountedAmount);
            } catch (Exception e){
                account.charge(detail.getAmount());
            }
        }
        PayPalTransaction transaction;
        restaurantAccount.setBalance(restaurantAccount.getBalance() + detail.getAmount());
        if(discountedAmount == 0f) {
            transaction = new PayPalTransaction(detail.getAmount(), payment.getCurrency().toString(), detail.getUserId(), restaurantAccount.getRestaurantId(), payment.getPaymentType().toString(), account.getId());
        } else {
            transaction = new PayPalTransaction(discountedAmount, payment.getCurrency().toString(), detail.getUserId(), restaurantAccount.getRestaurantId(), payment.getPaymentType().toString(), account.getId());
        }
        bankAccountRepository.save(restaurantAccount);
        paypalAccountRepository.save(account);
        payPalTransactionRepository.save(transaction);
        return checkStatusAndSendEmail(detail);
    }

    // Solo gli employee del ristorante devono poter consentire il pagamento con contanti
    @Transactional
    protected ResponseEntity payWithCash(PaymentDetail detail, int employeeId) throws Exception {
        Payment payment = paymentRepository.findById(detail.getPaymentId()).orElse(null);
        if(payment == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Non è stato possibile trovare il pagamento associato!");

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8087/employee/" + employeeId);
        Employee employee = null;
        try{
            employee = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(Employee.class);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Non è stato possibile trovare il dipendente!");
        }

        BankAccount restaurantAccount = bankAccountRepository.findById(payment.getRestaurantPayeeId()).orElse(null);
        if(restaurantAccount == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Impossibile effettuare un pagamento a questo ristorante: conto bancario non trovato!");

        restaurantAccount.setBalance(restaurantAccount.getBalance() + detail.getAmount());
        try{
            ScoreService.userSetScore(detail.getAmount(), detail.getUserId());
        } catch (Exception e){}
        CashTransaction transaction = new CashTransaction(detail.getAmount(), payment.getCurrency().toString(), detail.getUserId(), employee.getRestaurantId(), payment.getPaymentType().toString(), employee.getId());
        bankAccountRepository.save(restaurantAccount);
        cashTransactionRepository.save(transaction);
        client.close();
        return checkStatusAndSendEmail(detail);
    }

    private ResponseEntity checkStatusAndSendEmail(PaymentDetail detail){
        Payment payment = paymentRepository.findById(detail.getPaymentId()).orElse(null);
        if(payment.getPaymentType() == PaymentType.PERSONALLY_SPLIT)
            checkPaymentStatusPersonallySplit(detail);
        else
            checkPaymentStatus(detail);
        if(sendProcessedPaymentEmail(detail).getStatusCode() != HttpStatus.CREATED)
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Il pagamento è stato effettuato correttamente ma non è stato possibile inviare l'email di conferma!");

        return ResponseEntity.ok().body("Pagamento effettuato correttamente!");
    }

    private ResponseEntity checkPaymentStatus(PaymentDetail detail){
        detail.togglePaid();
        paymentDetailRepository.save(detail);

        Payment payment = paymentRepository.findById(detail.getPaymentId()).orElse(null);
        if(payment == null) //Non dovrebbe accadere mai
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile trovare il pagamento associato!");
        switch (payment.getStatus()){
            case NOT_PAYED:
                payment.setStatus("PARTIALLY_PAID");
                paymentRepository.save(payment);
                break;
            case PARTIALLY_PAID:
                Iterable<PaymentDetail> details = paymentDetailRepository.findByPaymentId(detail.getId());
                boolean allPaid = true;
                for (PaymentDetail pd : details) {
                    if (!pd.isPaid()) {
                        allPaid = false;
                        break;
                    }
                }
                if (allPaid) {
                    payment.setStatus("PAID");
                    paymentRepository.save(payment);
                    break;
                }
            case PAID:
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + payment.getStatus());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private ResponseEntity checkPaymentStatusPersonallySplit(PaymentDetail detail){
        detail.togglePaid();
        paymentDetailRepository.save(detail);

        Payment payment = paymentRepository.findById(detail.getPaymentId()).orElse(null);
        if(payment == null) //Non dovrebbe accadere mai
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile trovare il pagamento associato!");
        switch (payment.getStatus()){
            case NOT_PAYED:
                payment.setStatus("PARTIALLY_PAID");
                paymentRepository.save(payment);
                break;

            case PARTIALLY_PAID:
                Iterable<PaymentDetail> details = paymentDetailRepository.findByPaymentId(detail.getId());
                float paidAmount = 0;
                int totalUsers = 0;
                int usersCount = 0;
                for (PaymentDetail pd : details) {
                    totalUsers++;
                    if(pd.isPaid()){
                        paidAmount += pd.getAmount();
                        usersCount++;
                    }
                }
                if(paidAmount == payment.getAmount() && usersCount != totalUsers){
                    payment.setStatus("PAID");
                    paymentRepository.save(payment);
                    return ResponseEntity.status(HttpStatus.OK).body("Il pagamento è completo ma non tutti gli utenti hanno pagato!");
                }
                if(paidAmount == payment.getAmount() && usersCount == totalUsers){
                    payment.setStatus("PAID");
                    paymentRepository.save(payment);
                }
                break;

            case PAID:
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + payment.getStatus());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private ResponseEntity sendProcessedPaymentEmail(PaymentDetail detail){
        Client client = ClientBuilder.newClient();

        WebTarget resource = client.target("http://localhost:8087/customer/" + detail.getUserId());
        Customer customer;
        try {
            Response response = resource.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 200) {
                customer = response.readEntity(Customer.class);
            } else {
                System.err.println("Errore nella richiesta: " + response.getStatus() + " " + response.getStatusInfo());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Non è stato possibile trovare l'user con id " + detail.getUserId());
            }
        } catch (Exception e) {
            System.err.println("Errore durante la richiesta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            String email = URLEncoder.encode(customer.getEmail(), "UTF-8");
            String subject = URLEncoder.encode("Pagamento effettuato con successo", "UTF-8");

            resource = client.target("http://localhost:8088/email/send?to=" + email + "&subject=" + subject);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String body = "La transazione relativa al pagamento " + detail.getId() + " è andata a buon fine!\n\nGrazie per esserti affidato a MealHero e ai nostri ristoranti!";
        Response response = resource.request().post(Entity.text(body));
        if (!(response.getStatus() == 201))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile inviare l'email a " + customer.getEmail() + " --> Response status: " + response.getStatus());

        client.close();
        return ResponseEntity.status(HttpStatus.CREATED).body("Email inviata con successo a " + customer.getEmail());
    }

    private float calculateUserAmount(int userId){
        Iterable<UserOrderDetail> ods = userOrderDetailRepository.findByUserId(userId);

        float amount = 0f;

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8082/menuItem/");
        for(UserOrderDetail od : ods){
            try{
                WebTarget resource = target.path(Integer.toString(od.getMenuItemId()));
                MenuItem item = resource.request()
                        .accept(MediaType.APPLICATION_JSON)
                        .get(MenuItem.class);

                if(item == null)
                    return 0f;

                amount += (float) (item.getPrice()*od.getQuantity());
            } catch (Exception e){
                System.err.println("Errore nella richiesta: " + e.getMessage());
                return 0f;
            }
        }
        return amount;
    }
}


class MenuItem {

    private int id;
    private String name;
    private String description;
    private double price;
    private int menuId;

    protected MenuItem(){}

    public MenuItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
}

class Employee{
    private int id;
    private String username;
    private String email;
    private String password;
    private String role;
    private int restaurantId;

    protected Employee() {this.role = "EMPLOYEE";}

    public Employee(String username, String email, String password, int restaurantId) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.restaurantId = restaurantId;
        this.role = "EMPLOYEE";
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

