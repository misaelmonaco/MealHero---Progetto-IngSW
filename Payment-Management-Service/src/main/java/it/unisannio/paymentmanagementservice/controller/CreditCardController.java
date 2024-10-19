package it.unisannio.paymentmanagementservice.controller;

import it.unisannio.paymentmanagementservice.model.entity.CreditCard;
import it.unisannio.paymentmanagementservice.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/creditCard")
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @PostMapping("/add")
    public ResponseEntity addCreditCard(@RequestBody CreditCard creditCard) {
        return creditCardService.addCreditCard(creditCard);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCreditCard(@PathVariable Integer id) {
        return creditCardService.deleteCreditCard(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCreditCard(@PathVariable Integer id, @RequestBody CreditCard creditCard) {
        return creditCardService.updateCreditCard(id, creditCard);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCreditCardById(@PathVariable Integer id) {
        if(creditCardService.getCreditCardById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(creditCardService.getCreditCardById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllCreditCards() {
        if(!creditCardService.getAllCreditCards().iterator().hasNext())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(creditCardService.getAllCreditCards());
    }

    @GetMapping("/userId/{userId}")
    public ResponseEntity getCreditCardByUserId(@PathVariable Integer userId) {
        if(creditCardService.getByUserId(userId) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(creditCardService.getByUserId(userId));
    }

    @GetMapping("/cardNumber/{cardNumber}")
    public ResponseEntity getCreditCardByCardNumber(@PathVariable String cardNumber) {
        if(creditCardService.getCreditCardByCardNumber(cardNumber) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(creditCardService.getCreditCardByCardNumber(cardNumber) == null);
    }

    @GetMapping("/holderName/{holderName}")
    public ResponseEntity getCreditCardByHolderName(@PathVariable String holderName) {
        if(!creditCardService.getCreditCardByHolderName(holderName).iterator().hasNext())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(creditCardService.getCreditCardByHolderName(holderName));
    }
}
