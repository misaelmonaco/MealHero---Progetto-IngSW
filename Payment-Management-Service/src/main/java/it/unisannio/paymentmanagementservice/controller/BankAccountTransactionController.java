package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.model.entity.BankAccountTransaction;
import it.unisannio.paymentmanagementservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankAccountTransaction")
public class BankAccountTransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/add")
    public ResponseEntity addTransaction(@RequestBody BankAccountTransaction bankAccountTransaction) {
        return transactionService.addBankAccountTransaction(bankAccountTransaction);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteTransaction(@PathVariable Integer id) {
        return transactionService.deleteBankAccountTransaction(id);
    }

    @GetMapping("/all")
    public ResponseEntity getAllTransactions() {
        if(!transactionService.getAllBankAccountTransactions().iterator().hasNext())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(transactionService.getAllBankAccountTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity getTransactionById(@PathVariable Integer id) {
        if(transactionService.getBankAccountTransactionById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(transactionService.getBankAccountTransactionById(id));
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity getRestaurantTransactionById(@PathVariable Integer id) {
        if(!transactionService.getBankAccountTransactionsByRestaurantId(id).iterator().hasNext())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(transactionService.getBankAccountTransactionsByRestaurantId(id));
    }
}
