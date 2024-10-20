package it.unisannio.paymentmanagementservice.model.entity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Base64;

import it.unisannio.paymentmanagementservice.model.AESEncryption;
import jakarta.persistence.*;

@Entity
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, unique = true)
    private int userId;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String cardHolderName;

    @Column(nullable = false)
    private LocalDate expirationDate;

    @Column(nullable = false)
    private String cvv;

    @Column(nullable = false)
    private double balance;

    public CreditCard() {}

    public CreditCard(int userId, String cardNumber, String cardHolderName, LocalDate expirationDate, String cvv, double balance) throws Exception {
        this.userId = userId;
        this.cardNumber = AESEncryption.encrypt(Base64.getEncoder().encodeToString(cardNumber.getBytes()));
        this.cardHolderName = AESEncryption.encrypt(Base64.getEncoder().encodeToString(cardHolderName.getBytes()));;
        this.expirationDate = expirationDate;
        this.cvv = AESEncryption.encrypt(Base64.getEncoder().encodeToString(cvv.getBytes()));
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public String getCardNumber() throws Exception {
        return new String(Base64.getDecoder().decode(AESEncryption.decrypt(cardNumber)));
    }

    public void setCardNumber(String cardNumber) throws Exception {
        this.cardNumber = AESEncryption.encrypt(Base64.getEncoder().encodeToString(cardNumber.getBytes()));
    }

    public String getCardHolderName() throws Exception {
        return new String(Base64.getDecoder().decode(AESEncryption.decrypt(this.cardHolderName)));
    }

    public void setCardHolderName(String cardHolderName) throws Exception {
        this.cardHolderName = AESEncryption.encrypt(Base64.getEncoder().encodeToString(cardHolderName.getBytes()));
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }


    public String getCvv() throws Exception {
        return new String(Base64.getDecoder().decode(AESEncryption.decrypt(this.cvv)));
    }

    public void setCvv(String cvv) throws Exception {
        this.cvv = AESEncryption.encrypt(Base64.getEncoder().encodeToString(cvv.getBytes()));
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean hasSufficientBalance(double amount) throws Exception {
        return getBalance() >= amount;
    }

    public int getUserId() {
        return userId;
    }

    public void charge(double amount) throws Exception {
        if (hasSufficientBalance(amount)) {
            setBalance(this.balance - amount);
        } else {
            throw new IllegalArgumentException("Saldo insufficiente");
        }
    }
}
