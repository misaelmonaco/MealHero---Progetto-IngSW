package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.model.entity.Transaction;
import it.unisannio.paymentmanagementservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTransaction(@PathVariable Integer id) {
        return transactionService.deleteTransaction(id);
    }

    @GetMapping("/all")
    public ResponseEntity getAllTransactions() {
        Iterable<Transaction> transactions = transactionService.getAllTransactions();
        if(!transactions.iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity getTransactionById(@PathVariable Integer id) {
        if(transactionService.getTransactionById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity getTransactionsByRestaurantId(@PathVariable Integer id) {
        Iterable<Transaction> transactions = transactionService.getTransactionsByRestaurantId(id);
        if(!transactions.iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getTransactionsByUserId(@PathVariable Integer id) {
        Iterable<Transaction> transactions = transactionService.getTransactionsByUserId(id);
        if(!transactions.iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactions);
    }
}
