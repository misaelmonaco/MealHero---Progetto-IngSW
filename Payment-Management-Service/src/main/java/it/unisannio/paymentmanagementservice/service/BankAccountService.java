package it.unisannio.paymentmanagementservice.service;

import it.unisannio.paymentmanagementservice.model.AESEncryption;
import it.unisannio.paymentmanagementservice.model.entity.BankAccount;
import it.unisannio.paymentmanagementservice.repository.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class BankAccountService {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    public ResponseEntity addBankAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity deleteBankAccount(Integer id) {
        if(!bankAccountRepository.existsById(id))
            return ResponseEntity.notFound().build();
        bankAccountRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity updateBankAccount(int id, BankAccount bankAccount) throws Exception {
        BankAccount existingBankAccount = bankAccountRepository.findById(id).orElse(null);
        if (existingBankAccount == null)
            return ResponseEntity.notFound().build();

        existingBankAccount.setAccountHolderName(bankAccount.getAccountHolderName());
        existingBankAccount.setIban(bankAccount.getIban());
        existingBankAccount.setBalance(bankAccount.getBalance());
        bankAccountRepository.save(existingBankAccount);
        return ResponseEntity.ok().build();
    }

    public BankAccount findBankAccountById(Integer id) {
        return bankAccountRepository.findById(id).orElse(null);
    }

    public BankAccount findBankAccountByIban(String iban) throws Exception {
        return bankAccountRepository.findByIban(AESEncryption.encrypt(Base64.getEncoder().encodeToString(iban.getBytes()))).orElse(null);
    }

    public BankAccount findBankAccountByRestaurantId(int restaurantId) {
        return bankAccountRepository.findByRestaurantId(restaurantId).orElse(null);
    }

    public Iterable<BankAccount> findAllBankAccounts() {
        return bankAccountRepository.findAll();
    }


}
