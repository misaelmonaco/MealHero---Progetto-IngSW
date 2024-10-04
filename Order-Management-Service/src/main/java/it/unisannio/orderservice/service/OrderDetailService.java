package it.unisannio.orderservice.service;

import it.unisannio.orderservice.entity.OrderDetail;
import it.unisannio.orderservice.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public ResponseEntity addOrderDetail(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);
        return ResponseEntity.status(HttpStatus.CREATED).body("Dettaglio di ordine aggiunto con successo!");
    }

    public ResponseEntity deleteOrderDetail(int id) {
        orderDetailRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Dettaglio di ordine rimosso con successo!");
    }

    public ResponseEntity updateOrderDetail(OrderDetail orderDetail, int id) {
        OrderDetail oldOrderDetail = orderDetailRepository.findById(id).orElse(null);
        if (oldOrderDetail == null) {
            return ResponseEntity.notFound().build();
        }

        oldOrderDetail.setQuantity(orderDetail.getQuantity());
        orderDetailRepository.save(oldOrderDetail);
        return ResponseEntity.status(HttpStatus.OK).body("Dettaglio di ordine modificato con successo!" );
    }

    public OrderDetail getOrderDetail(int id) {
        return orderDetailRepository.findById(id).orElse(null);
    }

    public Iterable<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
    }

    public Iterable<OrderDetail> getOrderDetailsByOrderId(int id) {
        return orderDetailRepository.findByOrderId(id);
    }
}
