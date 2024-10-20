package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.service.PaymentDetailService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/paymentDetail")
public class PaymentDetailController {

    @Autowired
    private PaymentDetailService paymentDetailService;

    @PutMapping("/updatePayment/{paymentDetailId}/{method}")
    public ResponseEntity updatePaymentDetail(@PathVariable Integer paymentDetailId, @PathVariable String method) {
        return paymentDetailService.updatePaymentDetail(paymentDetailId, method);
    }

    @PutMapping("/pay/{paymentDetailId}")
    public ResponseEntity pay(@PathVariable Integer paymentDetailId, @RequestParam(value = "employee", required = false) Integer employeeId) throws Exception {
        return paymentDetailService.pay(paymentDetailId, employeeId);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPaymentDetail(@PathVariable Integer id) {
        if(paymentDetailService.getById(id) == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(paymentDetailService.getById(id));
    }

    @GetMapping("/payment/{paymentId}")
    public ResponseEntity getByPaymentId(@PathVariable Integer paymentId) {
        if(paymentDetailService.getByPaymentId(paymentId) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(paymentDetailService.getByPaymentId(paymentId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getByUserId(@PathVariable Integer userId) {
        if(paymentDetailService.getByUserId(userId) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(paymentDetailService.getByUserId(userId));
    }

    @GetMapping("/all")
    public ResponseEntity getAllPaymentDetails() {
        if(!(paymentDetailService.getAllDetails().iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.OK).body(paymentDetailService.getAllDetails());
    }
}
