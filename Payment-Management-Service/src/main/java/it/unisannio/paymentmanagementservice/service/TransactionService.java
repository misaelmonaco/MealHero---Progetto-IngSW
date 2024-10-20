package it.unisannio.paymentmanagementservice.service;

import it.unisannio.paymentmanagementservice.model.entity.*;
import it.unisannio.paymentmanagementservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CardTransactionRepository cardTransactionRepository;

    @Autowired
    private CashTransactionRepository cashTransactionRepository;

    @Autowired
    private PayPalTransactionRepository payPalTransactionRepository;;

    @Autowired
    private BankAccountTransactionRepository bankAccountTransactionRepository;

    public ResponseEntity addCardTransaction(CardTransaction cardTransaction) {
        transactionRepository.save(cardTransaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity addCashTransaction(CashTransaction cashTransaction) {
        transactionRepository.save(cashTransaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity addPayPalTransaction(PayPalTransaction payPalTransaction) {
        transactionRepository.save(payPalTransaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity addBankAccountTransaction(BankAccountTransaction bankAccountTransaction) {
        bankAccountTransactionRepository.save(bankAccountTransaction);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public ResponseEntity deleteTransaction(int id){
        if(!transactionRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        transactionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteCardTransaction(int id){
        if(!cardTransactionRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        cardTransactionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteCashTransaction(int id){
        if(!cashTransactionRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        cashTransactionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deletePayPalTransaction(int id){
        if(!payPalTransactionRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        payPalTransactionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteBankAccountTransaction(int id){
        if(!bankAccountTransactionRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        bankAccountTransactionRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public Iterable<Transaction> getAllTransactions(){
        return transactionRepository.findAll();
    }

    public Iterable<CardTransaction> getAllCardTransactions(){
        return cardTransactionRepository.findAll();
    }

    public Iterable<CashTransaction> getAllCashTransactions(){
        return cashTransactionRepository.findAll();
    }

    public Iterable<PayPalTransaction> getAllPayPalTransactions(){
        return payPalTransactionRepository.findAll();
    }

    public Iterable<BankAccountTransaction> getAllBankAccountTransactions(){
        return bankAccountTransactionRepository.findAll();
    }

    public Transaction getTransactionById(int id){
        if(!transactionRepository.existsById(id)){
            return null;
        }
        return transactionRepository.findById(id).orElse(null);
    }

    public CardTransaction getCardTransactionById(int id){
        if(!cardTransactionRepository.existsById(id)){
            return null;
        }
        return cardTransactionRepository.findById(id).orElse(null);
    }

    public CashTransaction getCashTransactionById(int id){
        if(!cashTransactionRepository.existsById(id)){
            return null;
        }
        return cashTransactionRepository.findById(id).orElse(null);
    }

    public PayPalTransaction getPayPalTransactionById(int id){
        if(!payPalTransactionRepository.existsById(id)){
            return null;
        }
        return payPalTransactionRepository.findById(id).orElse(null);
    }

    public BankAccountTransaction getBankAccountTransactionById(int id){
        if(!bankAccountTransactionRepository.existsById(id)){
            return null;
        }
        return bankAccountTransactionRepository.findById(id).orElse(null);
    }

    public Iterable<Transaction> getTransactionsByRestaurantId(int id){
        if(!transactionRepository.findByRestaurantId(id).iterator().hasNext()){
            return null;
        }
        return transactionRepository.findByRestaurantId(id);
    }

    public Iterable<Transaction> getTransactionsByUserId(int id){
        if(!transactionRepository.findByUserId(id).iterator().hasNext()){
            return null;
        }
        return transactionRepository.findByUserId(id);
    }

    public Iterable<BankAccountTransaction> getBankAccountTransactionsByRestaurantId(int id){
        if(!bankAccountTransactionRepository.findByRestaurantId(id).iterator().hasNext()){
            return null;
        }
        return bankAccountTransactionRepository.findByRestaurantId(id);
    }
}
