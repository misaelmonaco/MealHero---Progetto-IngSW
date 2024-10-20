package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.AESEncryption;
import jakarta.persistence.*;

import java.util.Base64;

@Entity
public class PayPalAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, unique = true)
    private int userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private double balance;

    public PayPalAccount() {}

    public PayPalAccount(int userId, String email, String password, double balance) throws Exception {
        this.userId = userId;
        this.email = AESEncryption.encrypt(Base64.getEncoder().encodeToString(email.getBytes()));;
        this.password = AESEncryption.encrypt(Base64.getEncoder().encodeToString(password.getBytes()));
        this.balance = balance;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() throws Exception {
        return new String(Base64.getDecoder().decode(AESEncryption.decrypt(this.email)));
    }

    public void setEmail(String email) throws Exception {
        this.email = AESEncryption.encrypt(Base64.getEncoder().encodeToString(email.getBytes()));
    }

    public String getPassword() throws Exception {
        return new String(Base64.getDecoder().decode(AESEncryption.decrypt(this.password)));
    }

    public void setPassword(String password) throws Exception {
        this.password = AESEncryption.encrypt(Base64.getEncoder().encodeToString(password.getBytes()));
    }

    public double getBalance() throws Exception {
        return this.balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setBalance(double balance) throws Exception {
        this.balance = balance;
    }

    public boolean hasSufficientBalance(double amount) throws Exception {
        return this.balance >= amount;
    }

    public void charge(double amount) throws Exception {
        if (hasSufficientBalance(amount)) {
            setBalance(this.balance - amount);
        } else {
            throw new IllegalArgumentException("Saldo PayPal insufficiente");
        }
    }
}
