package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.entity.Payment;
import it.unisannio.paymentmanagementservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/add")
    public ResponseEntity addPayment(@RequestBody Payment payment, @RequestParam List<Integer> userIds) {
        return paymentService.addPayment(payment, userIds);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePayment(@PathVariable Integer id) {
        return paymentService.deletePaymentById(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPayment(@PathVariable Integer id) {
        if(paymentService.getPaymentById(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagamento non trovato!");
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllPayments() {
        if(!paymentService.getAllPayments().iterator().hasNext())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagamenti non trovati!");
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getAllPayments());
    }

    @GetMapping("/reservation/{id}")
    public ResponseEntity getPaymentByReservation(@PathVariable Integer id) {
        if(paymentService.getPaymentByReservationId(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagamento non trovato!");
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentByReservationId(id));
    }

   @GetMapping("/restaurant/{id}")
   public ResponseEntity getPaymentByRestaurant(@PathVariable Integer id) {
        if(!paymentService.getPaymentsByRestaurantId(id).iterator().hasNext())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pagamenti non trovati!");
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentsByRestaurantId(id));
   }
}
