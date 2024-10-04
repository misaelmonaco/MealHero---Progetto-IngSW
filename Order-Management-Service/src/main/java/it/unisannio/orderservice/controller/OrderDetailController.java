package it.unisannio.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.unisannio.orderservice.entity.OrderDetail;
import it.unisannio.orderservice.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/add")
    @Operation(description = "Il metodo è utilizzabile SOLO in fase di DEBUGGING --> Crea OrderDetails non connessi a uno specifico Order")
    public ResponseEntity addOrderDetail(@RequestBody OrderDetail orderDetail) {
        orderDetailService.addOrderDetail(orderDetail);

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.WARNING, "299 - Warning - È stato creato un OrderDetail non associato ad alcun Ordine!")
                .body("ATTENZIONE! È stato creato un OrderDetail non associato ad alcun Ordine!");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteOrderDetail(@PathVariable int id) {
        if(orderDetailService.getOrderDetail(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dettaglio di ordine con id " + id + " non trovato!");
        return orderDetailService.deleteOrderDetail(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateOrderDetail(@PathVariable int id, @RequestBody OrderDetail orderDetail) {
        return orderDetailService.updateOrderDetail(orderDetail, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getOrderDetail(@PathVariable int id) {
        if(orderDetailService.getOrderDetail(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDetailService.getOrderDetail(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllOrderDetails() {
        if(!(orderDetailService.getAllOrderDetails().iterator().hasNext())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDetailService.getAllOrderDetails());
    }

    @GetMapping("/order/{id}")
    public ResponseEntity getOrderDetailByOrderId(@PathVariable int id) {
        if(!(orderDetailService.getOrderDetailsByOrderId(id).iterator().hasNext())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(orderDetailService.getOrderDetailsByOrderId(id));
    }
}
