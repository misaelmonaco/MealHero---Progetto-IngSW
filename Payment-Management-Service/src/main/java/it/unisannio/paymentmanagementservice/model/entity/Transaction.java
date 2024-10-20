package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.Currency;
import it.unisannio.paymentmanagementservice.model.PaymentMethod;
import it.unisannio.paymentmanagementservice.model.PaymentType;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private double amount;

    private Currency currency;

    private int userId;

    private int restaurantId;

    private LocalDateTime transactionDateTime;

    private PaymentMethod paymentMethod;

    private PaymentType paymentType;

    protected Transaction() {}

    public Transaction(double amount, String currency, int userId, int restaurantId, String paymentType) {
        this.amount = amount;
        this.currency = Currency.valueOf(currency);
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.paymentType = PaymentType.valueOf(paymentType);
        this.transactionDateTime = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public LocalDateTime getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(LocalDateTime transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }
}
