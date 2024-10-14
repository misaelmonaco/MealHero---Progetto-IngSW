package it.unisannio.paymentmanagementservice.service;

import it.unisannio.paymentmanagementservice.entity.UserOrderDetail;
import it.unisannio.paymentmanagementservice.repository.UserOrderDetailRepository;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Iterator;

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

    public ResponseEntity add(UserOrderDetail userOrderDetail) {
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
        WebTarget target = client.target("http://localhost:8084/orderDetail/menuItem/" + userOrderDetail.getMenuItemId() +"/orderId/" + userOrderDetail.getOrderId());
         OrderDetail detail = target.request()
                                .accept(MediaType.APPLICATION_JSON)
                                .get(OrderDetail.class);

         if(detail == null)
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Impossibile recuperare il dettaglio d'ordine associato!");

         if(alredySelected == detail.getQuantity())
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Per il dettaglio d'ordine " + userOrderDetail.getId() + " tutte le quantità risultano già state selezionate.");

         // Non dovrebbe verificarsi mai
         if(alredySelected > detail.getQuantity())
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERRORE: Per il dettaglio d'ordine " + userOrderDetail.getId() + " le quantità selezionate risultano maggiori di quelle effettivamente da pagare!.");

         if(userOrderDetail.getQuantity() > (detail.getQuantity() - alredySelected))
             return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE).body("Stai cercando di pagare una quantità maggiore di quella ancora da saldare!");

        userOrderDetailRepository.save(userOrderDetail);
        return ResponseEntity.ok().build();
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

    class OrderDetail {

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
