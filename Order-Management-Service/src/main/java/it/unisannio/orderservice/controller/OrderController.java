package it.unisannio.orderservice.controller;

import it.unisannio.orderservice.entity.Order;
import it.unisannio.orderservice.entity.OrderDetail;
import it.unisannio.orderservice.repository.OrderDetailRepository;
import it.unisannio.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @PostMapping("/add")
    public ResponseEntity addOrder(@RequestBody Order order) {
        return orderService.addOrder(order);
    }

    @PostMapping("/add/{reservationId}")
    public ResponseEntity addOrder(@PathVariable("reservationId") Integer reservationId, @RequestBody List<OrderDetail> details) {
        Order newOrder = new Order(reservationId);
        String responseBody = (String) (orderService.addOrder(newOrder)).getBody();
        int id = Integer.parseInt(responseBody.substring(responseBody.lastIndexOf(": ") + 2));
        details.forEach(detail -> detail.setOrderId(id));
        orderDetailRepository.saveAll(details);
        return ResponseEntity.ok().body("Ordine aggiunto con successo!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrder(@PathVariable("id") int id) {
        if(orderService.findById(id) == null)
            return ResponseEntity.notFound().build();
        return orderService.deleteOrder(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateOrder(@PathVariable("id") int id, @RequestBody List<OrderDetail> details) {
        return orderService.updateOrder(id, details);
    }

    @PutMapping("/addDetail/{id}")
    public ResponseEntity addOrderDetail(@PathVariable("id") int id, @RequestBody OrderDetail orderDetail) {
        return orderService.addOrderDetail(id, orderDetail);
    }

    @PutMapping("/toggle/{id}")
    public ResponseEntity toggleOrder(@PathVariable("id") int id) {
        return orderService.toggleOrder(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrderById(@PathVariable("id") int id) {
        if(orderService.findById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(orderService.findById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllOrders() {
        if(!(orderService.findAll().iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(orderService.findAll());
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity getOrdersByReservationId(@PathVariable("id") int id) {
        if(!(orderService.findByReservationId(id).iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(orderService.findByReservationId(id));
    }

   @GetMapping("/restaurant/{restaurantId}/date/{date}/time/{time}")
   public ResponseEntity getOrdersByRestaurantDateAndTime(@PathVariable("restaurantId") int id, @PathVariable("date") String date, @PathVariable("time") String time) {
        return orderService.selectOrdersByTimeDateAndRestaurant(id, time, date);
   }

   @GetMapping("/details/{orderId}")
    public ResponseEntity getOrderDetails(@PathVariable("orderId") int orderId){
        if(orderService.findById(orderId) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ordine con id " + orderId + " non trovato!");
        return orderService.getOrderDetails(orderId);
   }
}
