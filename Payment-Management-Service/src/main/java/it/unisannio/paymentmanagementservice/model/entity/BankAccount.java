package it.unisannio.paymentmanagementservice.model.entity;

import jakarta.persistence.*;

@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private int restaurantId;

    @Column(nullable = false, unique = true)
    private String iban;

    @Column(nullable = false)
    private String accountHolderName;

    @Column(nullable = false)
    private double balance;

    public BankAccount() {}

    public BankAccount(int restaurantId, String iban, String accountHolderName) {
        this.restaurantId = restaurantId;
        this.iban = iban;
        this.accountHolderName = accountHolderName;
        this.balance = 0;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    public int getRestaurantId() {
        return restaurantId;
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
            System.out.println("Addebitato " + amount + " dal conto bancario. Nuovo saldo: " + this.balance);
        } else {
            throw new IllegalArgumentException("Saldo bancario insufficiente.");
        }
    }
}
