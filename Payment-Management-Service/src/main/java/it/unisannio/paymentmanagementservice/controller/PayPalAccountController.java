package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.model.entity.PayPalAccount;
import it.unisannio.paymentmanagementservice.service.PayPalAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/PayPalAccount")
public class PayPalAccountController {

    @Autowired
    private PayPalAccountService payPalAccountService;

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody PayPalAccount payPalAccount) {
        return payPalAccountService.addPayPalAccount(payPalAccount);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity delete(@PathVariable Integer id) {
        return payPalAccountService.deletePayPalAccount(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity update(@PathVariable Integer id, @RequestBody PayPalAccount payPalAccount) {
        return payPalAccountService.updatePayPalAccount(id, payPalAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable Integer id) {
        if(payPalAccountService.getPayPalAccountById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(payPalAccountService.getPayPalAccountById(id));
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity getByUserId(@PathVariable Integer userId) {
        if(payPalAccountService.getPayPalAccountByUserId(userId) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(payPalAccountService.getPayPalAccountByUserId(userId));
    }

    @GetMapping("/all")
    public ResponseEntity getAll() {
        if(!payPalAccountService.getAllPayPalAccounts().iterator().hasNext())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(payPalAccountService.getAllPayPalAccounts());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity getByEmail(@PathVariable String email) {
        if(payPalAccountService.getPayPalAccountByEmail(email) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(payPalAccountService.getPayPalAccountByEmail(email));
    }
}
