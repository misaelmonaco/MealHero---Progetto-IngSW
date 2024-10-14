package it.unisannio.paymentmanagementservice.entity;

import jakarta.persistence.*;

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

    public PayPalAccount(int userId, String email, String password, double balance) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.balance = balance;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public int getUserId() {
        return userId;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean hasSufficientBalance(double amount) {
        return this.balance >= amount;
    }

    public void charge(double amount) {
        if (hasSufficientBalance(amount)) {
            this.balance -= amount;
        } else {
            throw new IllegalArgumentException("Saldo PayPal insufficiente");
        }
    }
}
