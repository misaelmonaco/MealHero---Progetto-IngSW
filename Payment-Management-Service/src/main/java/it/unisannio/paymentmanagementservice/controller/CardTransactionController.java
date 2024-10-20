package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.model.entity.CardTransaction;
import it.unisannio.paymentmanagementservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cardTransaction")
public class CardTransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity addCardTransaction(@RequestBody CardTransaction cardTransaction) {
        return transactionService.addCardTransaction(cardTransaction);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCardTransaction(@PathVariable Integer id) {
        return transactionService.deleteCardTransaction(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCardTransaction(@PathVariable Integer id) {
        if(transactionService.getCardTransactionById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(transactionService.getCardTransactionById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllCardTransactions() {
        Iterable<CardTransaction> cardTransactions = transactionService.getAllCardTransactions();
        if(!cardTransactions.iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(cardTransactions);
    }
}
