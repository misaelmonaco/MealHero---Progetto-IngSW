package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.model.entity.BankAccount;
import it.unisannio.paymentmanagementservice.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bankAccount")
public class BankAccountController {

    @Autowired
    private BankAccountService bankAccountService;

    @PostMapping("/add")
    public ResponseEntity addBankAccount(@RequestBody BankAccount bankAccount) {
        return bankAccountService.addBankAccount(bankAccount);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteBankAccount(@PathVariable Integer id) {
        return bankAccountService.deleteBankAccount(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateBankAccount(@PathVariable Integer id, @RequestBody BankAccount bankAccount) throws Exception {
        return bankAccountService.updateBankAccount(id, bankAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity getBankAccount(@PathVariable Integer id) {
        if(bankAccountService.findBankAccountById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bankAccountService.findBankAccountById(id));
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity getBankAccountByRestaurantId(@PathVariable Integer id) {
        if(bankAccountService.findBankAccountByRestaurantId(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bankAccountService.findBankAccountByRestaurantId(id));
    }

    @GetMapping("/iban/{iban}")
    public ResponseEntity getBankAccountByIban(@PathVariable String iban) throws Exception {
        if(bankAccountService.findBankAccountByIban(iban) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bankAccountService.findBankAccountByIban(iban));
    }

    @GetMapping("/all")
    public ResponseEntity getAllBankAccounts() {
        if(!bankAccountService.findAllBankAccounts().iterator().hasNext())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(bankAccountService.findAllBankAccounts());
    }
}
