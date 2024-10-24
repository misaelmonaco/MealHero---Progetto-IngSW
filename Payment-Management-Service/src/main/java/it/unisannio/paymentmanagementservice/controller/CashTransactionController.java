package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.model.entity.CashTransaction;
import it.unisannio.paymentmanagementservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cashTransaction")
public class CashTransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity addCashTransaction(@RequestBody CashTransaction cashTransaction) {
        return transactionService.addCashTransaction(cashTransaction);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCashTransaction(@PathVariable Integer id) {
        return transactionService.deleteCashTransaction(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCashTransaction(@PathVariable Integer id) {
        if(transactionService.getCashTransactionById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transactionService.getCashTransactionById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllCashTransactions() {
        Iterable<CashTransaction> cashTransactions = transactionService.getAllCashTransactions();
        if(!cashTransactions.iterator().hasNext()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cashTransactions);
    }
}
