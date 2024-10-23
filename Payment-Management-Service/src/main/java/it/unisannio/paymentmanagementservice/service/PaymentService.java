package it.unisannio.paymentmanagementservice.service;

import it.unisannio.paymentmanagementservice.model.Customer;
import it.unisannio.paymentmanagementservice.model.PaymentStatus;
import it.unisannio.paymentmanagementservice.model.entity.Payment;
import it.unisannio.paymentmanagementservice.model.entity.PaymentDetail;
import it.unisannio.paymentmanagementservice.repository.PaymentDetailRepository;
import it.unisannio.paymentmanagementservice.repository.PaymentRepository;
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
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static it.unisannio.paymentmanagementservice.model.PaymentType.*;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    public ResponseEntity addPayment(Payment payment, List<Integer> userIds) {

        ResponseEntity response = ResponseEntity.internalServerError().body("Non è stato possibile effettuare il pagamento!");

        if(paymentRepository.getPaymentByReservation(payment.getReservationID()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Risulta già creato un pagamento per la prenotazione selezionata!") ;

        if(userIds.isEmpty())
            return ResponseEntity.internalServerError().body("Non sono stati specificati gli users che devono pagare!");

        if(payment.getPaymentType() == ALL_IN_ONE && userIds.size() == 1)
            response = all_in_one_payment(payment, userIds);

        if(payment.getPaymentType() == EQUALLY_SPLIT && userIds.size() != 1)
            response = equally_split_payment(payment, userIds);

        if(payment.getPaymentType() == EQUALLY_SPLIT && userIds.size() == 1){
            payment.setPaymentType("ALL_IN_ONE");
            response = all_in_one_payment(payment, userIds);
        }

        if(payment.getPaymentType() == PERSONALLY_SPLIT && userIds.size() != 1)
            response = personally_split_payment(payment, userIds);

        if(payment.getPaymentType() == PERSONALLY_SPLIT && userIds.size() == 1){
            payment.setPaymentType("ALL_IN_ONE");
            response = all_in_one_payment(payment, userIds);
        }

        return response;
    }

    public ResponseEntity deletePaymentById(Integer id) {
        if(!paymentRepository.existsById(id))
            return ResponseEntity.notFound().build();
        paymentRepository.deleteById(id);
        paymentDetailRepository.deleteByPaymentId(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public Iterable<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Payment getPaymentById(int id) {
        return paymentRepository.findById(id).orElse(null);
    }

    public Iterable<Payment> getPaymentsByRestaurantId(int id) {
        return paymentRepository.getPaymentsByRestaurant(id);
    }

    public Payment getPaymentByReservationId(int id) {
        return paymentRepository.getPaymentByReservation(id);
    }

    /**
     * L'idea è che quando si crea un Payment il metodo richieda nel body della request una lista di user
     * che ne partecipano: il framework va a calcolarne le divisioni (generando un PaymentDetail per user) e manda una mail
     * di avviso a tutti gli utenti interessati che devono effettuare il pagamento. Questi devono recarsi nella
     * loro area personale, selezionare il giusto PaymentDetail (tramite l'id fornito nella mail)
     * selezionare il metodo di pagamento (questo modificherà PaymentDetail) e
     * pagare (non per forza nello stesso momento). Una volta pagato il singolo PaymentDetail, verrà mandata
     * una mail di conferma all'user interessato e cambierà (se non già cambiato) lo stato del corrispettivo Payment. Quando
     * tutti i PaymentDetail saranno pagati, verrà cambiato lo stato del Payment in PAID.
     */

    private ResponseEntity all_in_one_payment(Payment payment, List<Integer> userIds) {

        payment.setStatus("NOT_PAID");
        paymentRepository.save(payment);
        PaymentDetail detail = new PaymentDetail(userIds.get(0), payment.getId(), payment.getAmount());
        detail.setStatus(PaymentStatus.NOT_PAID);
        detail.setType(ALL_IN_ONE);
        paymentDetailRepository.save(detail);

        ArrayList<String> email = new ArrayList<>();
        ResponseEntity response = findUserEmail(userIds.get(0), payment, email);
        if(!(response.getStatusCode().is2xxSuccessful())){
            paymentDetailRepository.deleteByPaymentId(payment.getId());
            paymentRepository.delete(payment);
            return response;
        }

        response = null;

        response = sendPaymentEmail(email);
        if(!(response.getStatusCode().is2xxSuccessful())){
            paymentDetailRepository.delete(detail);
            paymentRepository.delete(payment);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Registrazione del pagamento fallita --> Non è stato possibile inviare una mail all'utente!");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Pagamento registrato con successo! Ora i CUSTOMER possono controllare l'area personale per scegliere come effettuare la transazione!\n\nPayment id: " + payment.getId());
    }

    @Transactional
    protected ResponseEntity equally_split_payment(Payment payment, List<Integer> userIds) {

        payment.setStatus("NOT_PAID");
        paymentRepository.save(payment);
        BigDecimal amount = payment.getAmount().divide(BigDecimal.valueOf(Double.parseDouble(String.valueOf(userIds.size()))), 2, RoundingMode.HALF_UP);
        BigDecimal remainder = payment.getAmount().remainder(BigDecimal.valueOf(Double.parseDouble(String.valueOf(userIds.size()))));
        int i = 0;
        int remainderValue = remainder.intValue();

        ArrayList<String> emails = new ArrayList<>();
        for (Integer userId : userIds) {
            PaymentDetail detail;
            if( i < remainderValue){
                i++;
                detail = new PaymentDetail(userId, payment.getId(), amount.add(new BigDecimal("0.01")));
            } else
                detail = new PaymentDetail(userId, payment.getId(), amount);
            detail.setStatus(PaymentStatus.NOT_PAID);
            detail.setType(EQUALLY_SPLIT);
            paymentDetailRepository.save(detail);

            ResponseEntity response = findUserEmail(userId, payment, emails);
            if(!response.getStatusCode().is2xxSuccessful()){
                paymentDetailRepository.deleteByPaymentId(payment.getId());
                paymentRepository.delete(payment);
                return response;
            }
        }

        ResponseEntity response = sendPaymentEmail(emails);
        if(!(response.getStatusCode().is2xxSuccessful())){
            paymentDetailRepository.deleteByPaymentId(payment.getId());
            paymentRepository.delete(payment);
            return response;
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("Pagamento registrato con successo! Ora i CUSTOMER possono controllare l'area personale per scegliere come effettuare la transazione!\n\nPayment id: " + payment.getId());
    }

    private ResponseEntity personally_split_payment(Payment payment, List<Integer> userIds) {
        payment.setStatus("NOT_PAID");
        paymentRepository.save(payment);

        ArrayList<String> emails = new ArrayList<>();
        for (Integer userId : userIds) {
            PaymentDetail detail = new PaymentDetail(userId, payment.getId(), BigDecimal.ZERO);
            detail.setStatus(PaymentStatus.NOT_PAID);
            detail.setType(PERSONALLY_SPLIT);
            paymentDetailRepository.save(detail);

            ResponseEntity response = findUserEmail(userId, payment, emails);
            if(!response.getStatusCode().is2xxSuccessful()){
                paymentDetailRepository.deleteByPaymentId(payment.getId());
                paymentRepository.delete(payment);
                return response;
            }
        }
        ResponseEntity response = sendPaymentEmail(emails);
        if(!(response.getStatusCode().is2xxSuccessful())){
            paymentDetailRepository.deleteByPaymentId(payment.getId());
            paymentRepository.delete(payment);
            return response;
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Pagamento registrato con successo! Ora i CUSTOMER possono controllare l'area personale per scegliere come effettuare la transazione!\n\nPayment id: " + payment.getId());
    }

    private ResponseEntity sendPaymentEmail(ArrayList<String> emails) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = null;
        try {
            StringBuilder emailBuilder = new StringBuilder();
            for (String email : emails) {
                if (emailBuilder.length() > 0) {
                    emailBuilder.append(",");
                }
                emailBuilder.append(URLEncoder.encode(email, "UTF-8"));
            }
            String subject = URLEncoder.encode("Nuovo Pagamento nella tua area personale", "UTF-8");
            resource = client.target("http://localhost:8088/email/sendMoreThanOne?to=" + emailBuilder + "&subject=" + subject);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String body = "Nella tua area personale è stato aggiunto un nuovo pagamento!\nAccedi a MealHero per scegliere come desideri effettuare e completare la transazione!" +
                "\n\nSe non trovi alcun pagamento nell'area personale, hai ricevuto questa mail per errore. \n\nQuesto è un indirizzo di posta no-reply. Non è possibile rispondere a questa email!";
        Response response = resource.request().post(Entity.text(body));
        if (!(response.getStatus() == 201))
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile inviare l'email a tutti i clienti! --> Response status: " + response.getStatus());

        client.close();
        return ResponseEntity.status(HttpStatus.CREATED).body("Email inviata con successo a " + emails.toString());
    }

    public ResponseEntity findUserEmail(int userId, Payment payment, ArrayList<String> emails) {
        Client client = ClientBuilder.newClient();
        WebTarget resource = client.target("http://localhost:8087/customer/" + userId);
        Customer customer;
        try {
            Response response = resource.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get();

            if (response.getStatus() == 200) {
                customer = response.readEntity(Customer.class);
                emails.add(customer.getEmail());
            } else {
                System.err.println("Errore nella richiesta: " + response.getStatus() + " " + response.getStatusInfo());
                paymentDetailRepository.deleteByPaymentId(payment.getId());
                paymentRepository.delete(payment);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Non è stato possibile trovare l'user con id " + userId + "\nResponse: " + response);
            }
        } catch (Exception e) {
            System.err.println("Errore durante la richiesta: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        client.close();
        return ResponseEntity.status(HttpStatus.OK).body(emails);
    }
}



