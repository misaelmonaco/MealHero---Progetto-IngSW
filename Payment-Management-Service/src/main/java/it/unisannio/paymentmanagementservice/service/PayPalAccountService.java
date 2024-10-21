package it.unisannio.paymentmanagementservice.service;

import it.unisannio.paymentmanagementservice.model.AESEncryption;
import it.unisannio.paymentmanagementservice.model.entity.PayPalAccount;
import it.unisannio.paymentmanagementservice.repository.PayPalAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class PayPalAccountService {

    @Autowired
    private PayPalAccountRepository payPalAccountRepository;

    public ResponseEntity addPayPalAccount(PayPalAccount payPalAccount) throws Exception {
        if(payPalAccountRepository.findByUserId(payPalAccount.getUserId()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Già esiste un account per l'user associato!");

        if(payPalAccountRepository.findByEmail(payPalAccount.getEmail()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Già esiste un account per l'email associata!");

        if(payPalAccount.getPassword().contains(" "))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La password non può contenere spazi vuoti!");

        payPalAccountRepository.save(payPalAccount);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity deletePayPalAccount(Integer id) {
        if(!payPalAccountRepository.existsById(id))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        payPalAccountRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity updatePayPalAccount(int id, PayPalAccount payPalAccount) throws Exception {
        PayPalAccount existingPayPalAccount = payPalAccountRepository.findById(id).orElse(null);
        if (existingPayPalAccount == null)
            return ResponseEntity.notFound().build();

        existingPayPalAccount.setEmail(payPalAccount.getEmail());
        existingPayPalAccount.setPassword(payPalAccount.getPassword());
        existingPayPalAccount.setBalance(payPalAccount.getBalance());
        payPalAccountRepository.save(existingPayPalAccount);
        return ResponseEntity.ok().build();
    }

    public PayPalAccount getPayPalAccountById(Integer id) {
        return payPalAccountRepository.findById(id).orElse(null);
    }

    public Iterable<PayPalAccount> getAllPayPalAccounts() {
        return payPalAccountRepository.findAll();
    }

    public PayPalAccount getPayPalAccountByEmail(String email) throws Exception {
        return payPalAccountRepository.findByEmail(AESEncryption.encrypt(Base64.getEncoder().encodeToString(email.getBytes()))).orElse(null);
    }

    public PayPalAccount getPayPalAccountByUserId(Integer userId) {
        return payPalAccountRepository.findByUserId(userId).orElse(null);
    }
}
