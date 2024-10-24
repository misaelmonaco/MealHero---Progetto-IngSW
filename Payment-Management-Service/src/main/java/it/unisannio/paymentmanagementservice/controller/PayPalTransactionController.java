package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.model.entity.PayPalTransaction;
import it.unisannio.paymentmanagementservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/PayPalTransaction")
public class PayPalTransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity addPayPalTransaction(@RequestBody PayPalTransaction payPalTransaction) {
        return transactionService.addPayPalTransaction(payPalTransaction);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletePayPalTransaction(@PathVariable Integer id) {
        return transactionService.deletePayPalTransaction(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPayPalTransaction(@PathVariable Integer id) {
        if(transactionService.getPayPalTransactionById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionService.getPayPalTransactionById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getPayPalTransactions() {
        Iterable<PayPalTransaction> payPalTransactions = transactionService.getAllPayPalTransactions();
        if(!payPalTransactions.iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(payPalTransactions);
    }
}
