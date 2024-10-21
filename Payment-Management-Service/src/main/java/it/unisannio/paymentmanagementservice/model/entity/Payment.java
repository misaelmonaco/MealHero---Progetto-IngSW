package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.Currency;
import it.unisannio.paymentmanagementservice.model.PaymentStatus;
import it.unisannio.paymentmanagementservice.model.PaymentType;
import jakarta.persistence.*;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private int reservationID;

    @Column(nullable = false)
    private int restaurantPayeeId;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    private Currency currency;

    @Column(nullable = false)
    private PaymentType paymentType;

    @Column(nullable = false)
    private PaymentStatus status;

    protected Payment() {}

    public Payment(int reservationID, int restaurantPayeeId, float amount, String currency, String paymentType) {
        this.reservationID = reservationID;
        this.restaurantPayeeId = restaurantPayeeId;
        this.amount = amount;
        this.currency = Currency.valueOf(currency);
        this.paymentType = PaymentType.valueOf(paymentType);
        this.status = PaymentStatus.NOT_PAID;
    }

    public int getId() {
        return id;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = Currency.valueOf(currency);
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = PaymentType.valueOf(paymentType);
    }

    public int getRestaurantPayeeId() {
        return restaurantPayeeId;
    }

    public void setRestaurantPayeeId(int restaurantPayeeId) {
        this.restaurantPayeeId = restaurantPayeeId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = PaymentStatus.valueOf(status);
    }
}
