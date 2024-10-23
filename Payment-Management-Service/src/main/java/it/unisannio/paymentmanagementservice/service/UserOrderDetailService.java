package it.unisannio.paymentmanagementservice.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.unisannio.paymentmanagementservice.model.PaymentStatus;
import it.unisannio.paymentmanagementservice.model.entity.Payment;
import it.unisannio.paymentmanagementservice.model.entity.PaymentDetail;
import it.unisannio.paymentmanagementservice.model.entity.UserOrderDetail;
import it.unisannio.paymentmanagementservice.repository.PaymentDetailRepository;
import it.unisannio.paymentmanagementservice.repository.PaymentRepository;
import it.unisannio.paymentmanagementservice.repository.UserOrderDetailRepository;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * L'idea è quella che, nel momento in cui viene selezionato come metodo di pagamento
 * il PERSONALLY SPLIT, venga mostrata a ogni utente (una volta che esso si trova nella sezione
 * pagamenti della propria area personale) una tabella con tutti gli items dell'ordine
 * che bisogna pagare con accanto un menù a tendina che permetta di selezionare la quantità e
 * un tasto "ADD" che permette la creazione di un oggetto UserOrderDetail.
 */
@Service
public class UserOrderDetailService {

    @Autowired
    UserOrderDetailRepository userOrderDetailRepository;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private PaymentDetailRepository paymentDetailRepository;

    public ResponseEntity add(UserOrderDetail userOrderDetail) {

        boolean result = false;
        ResponseEntity response = checkIfNOTAllSelected(userOrderDetail.getOrderId());
        if(response.getStatusCode().is2xxSuccessful()) { // Status OK indica che non è stato tutto selezionato e che si può procedere ancora alla selezione
            response = checkIfLastUserInSelection(userOrderDetail.getOrderId(), userOrderDetail.getUserId());
            if(response.getStatusCode().is2xxSuccessful()) { // Status OK indica che è l'ultimo user a selezionare quindi automaticamente gli si deve addebitare tutto quello che resta
                return processRemainingOrderDetails(userOrderDetail, userOrderDetail.getUserId());
            } if (response.getStatusCode().is5xxServerError()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile controllare se sei l'ultimo utente a selezionare gli items del menu!");
            } else{ // Altrimenti processa quello che l'utente ha chiesto di aggiungere
                Iterable<UserOrderDetail> userOrderDetails = userOrderDetailRepository.findByOrderId(userOrderDetail.getOrderId());
                Iterator<UserOrderDetail> iterator = userOrderDetails.iterator();
                while (iterator.hasNext()) {
                    UserOrderDetail orderDetail = iterator.next();
                    if (orderDetail.getMenuItemId() != userOrderDetail.getMenuItemId())
                        iterator.remove();
                }

                int alredySelected = 0;
                for (UserOrderDetail detail : userOrderDetails) {
                    alredySelected += detail.getQuantity();
                }

                Client client = ClientBuilder.newClient();
                WebTarget target = client.target("http://localhost:8084/orderDetail/menuItem/" + userOrderDetail.getMenuItemId() + "/orderId/" + userOrderDetail.getOrderId());
                OrderDetail detail = target.request()
                        .accept(MediaType.APPLICATION_JSON)
                        .get(OrderDetail.class);

                if (detail == null)
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile recuperare il dettaglio d'ordine associato!");

                if (alredySelected == detail.getQuantity())
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Per il dettaglio d'ordine " + userOrderDetail.getId() + " tutte le quantità risultano già state selezionate.");

                // Non dovrebbe verificarsi mai
                if (alredySelected > detail.getQuantity())
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERRORE: Per il dettaglio d'ordine " + userOrderDetail.getId() + " le quantità selezionate risultano maggiori di quelle effettivamente da pagare!.");

                if (userOrderDetail.getQuantity() > (detail.getQuantity() - alredySelected))
                    return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body("Stai cercando di pagare una quantità maggiore di quella ancora da saldare!");

                userOrderDetailRepository.save(userOrderDetail);
                return ResponseEntity.ok().build();
            }
        } else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tutti gli items sono stati selezionati, non puoi pagare più nulla. \nSe non hai pagato nulla, il tuo pagamento verrà rimosso dalla tua area quando tutti avranno pagato!");
    }

    public ResponseEntity delete(int userOrderDetailId) {
        if(!userOrderDetailRepository.existsById(userOrderDetailId))
            return ResponseEntity.notFound().build();
        userOrderDetailRepository.deleteById(userOrderDetailId);
        return ResponseEntity.ok().build();
    }

    public UserOrderDetail findById(int userOrderDetailId) {
        return userOrderDetailRepository.findById(userOrderDetailId).orElse(null);
    }

    public Iterable<UserOrderDetail> findAll() {
        return userOrderDetailRepository.findAll();
    }

    public Iterable<UserOrderDetail> findByUserId(int userId) {
        return userOrderDetailRepository.findByUserId(userId);
    }

    private ResponseEntity checkIfNOTAllSelected(int orderId) {
        Client client = ClientBuilder.newClient();
        Iterable<UserOrderDetail> uod = userOrderDetailRepository.findByOrderId(orderId);
        WebTarget target = client.target("http://localhost:8084/orderDetail/order/" + orderId);
        Iterable<OrderDetail> orderDetails;
        try{
            orderDetails = target.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<Iterable<OrderDetail>>() {});
        } catch (ProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile recuperare i dettagli d'ordine!!");
        }
        int counter = 0;
        for (OrderDetail orderDetail : orderDetails) {
            List<UserOrderDetail> userOrderDetails = (List<UserOrderDetail>) userOrderDetailRepository.findByOrderIdAndMenuItemId(orderDetail.getOrderId(), orderDetail.getMenuItemId());
            int totalQuantityCovered = userOrderDetails.stream()
                    .mapToInt(UserOrderDetail::getQuantity)
                    .sum();

            if (totalQuantityCovered < orderDetail.getQuantity()) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
        }
        return ResponseEntity.internalServerError().build();
    }

    private ResponseEntity processRemainingOrderDetails(UserOrderDetail userOrderDetail, int userId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8084/orderDetail/order/" + userOrderDetail.getOrderId());
        Iterable<OrderDetail> details;
        try {
            details = target.request().accept(MediaType.APPLICATION_JSON).get(new GenericType<Iterable<OrderDetail>>() {
            });
        } catch (ProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile recuperare i dettagli d'ordine!");
        }

        int counter = 0;
        for (OrderDetail orderDetail : details) {
            List<UserOrderDetail> userOrderDetails = (List<UserOrderDetail>) userOrderDetailRepository.findByOrderIdAndMenuItemId(orderDetail.getOrderId(), orderDetail.getMenuItemId());
            int totalQuantityCovered = userOrderDetails.stream()
                    .mapToInt(UserOrderDetail::getQuantity)
                    .sum();

            if (totalQuantityCovered < orderDetail.getQuantity()) {
                int remainingQuantity = orderDetail.getQuantity() - totalQuantityCovered;

                UserOrderDetail uod = new UserOrderDetail(userId, orderDetail.getOrderId(), orderDetail.getMenuItemId(), remainingQuantity);
                counter++;
                userOrderDetailRepository.save(uod);
            }
        }

        if(counter == 0)
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("Tutti gli items sono già stati assegnati!");
        else
            return ResponseEntity.status(HttpStatus.CREATED).body("Essendo l'ultimo utente a poter selezionare cosa pagare, tutto ciò che è rimasto ti è stato addebitato!");
    }

    private ResponseEntity checkIfLastUserInSelection(int orderId, int userId) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8084/order/" + orderId);
        Order order;
        try{
            order = target.request().accept(MediaType.APPLICATION_JSON)
                    .get(Order.class);
        } catch (ProcessingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Payment payment = paymentRepository.getPaymentByReservation(order.getReservationId());
        ArrayList<PaymentDetail> details = new ArrayList<>((Collection) paymentDetailRepository.findByPaymentId(payment.getId()));
        ArrayList<UserOrderDetail> uod = new ArrayList<>((Collection) userOrderDetailRepository.findByOrderId(orderId));
        boolean last = true;
        for (PaymentDetail paymentDetail : details) {
            int id = paymentDetail.getUserId();
            if(id == userId)
               continue;
            boolean userHasOrderDetails = uod.stream()
                    .anyMatch(userOrderDetail -> (userOrderDetail.getUserId() == id));
            if (!userHasOrderDetails){
                last = false;
                break;
            }
       }
        if(last)
            return ResponseEntity.status(HttpStatus.OK).build(); // Non ci sono altri user che devono selezionare
                                                                 // ad eccezione di userId
        else
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // Ci sono altri user che ancora devono
                                                                       // selezionare ad esclusione di userId
    }

   static class OrderDetail {

        private int id;
        private int orderId;
        private int menuItemId;
        private int quantity;

        public OrderDetail(int menuItemId, int quantity, int orderId) {
            this.menuItemId = menuItemId;
            this.quantity = quantity;
            this.orderId = orderId;
        }

        public int getId() {
            return id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public int getMenuItemId() {
            return menuItemId;
        }

        public void setMenuItemId(int menuItemId) {
            this.menuItemId = menuItemId;
        }

        public int getOrderId() { return orderId; }

        public void setOrderId(int orderId) { this.orderId = orderId; }
    }

}
