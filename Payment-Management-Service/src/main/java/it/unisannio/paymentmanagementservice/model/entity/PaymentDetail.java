package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.PaymentMethod;
import it.unisannio.paymentmanagementservice.model.PaymentStatus;
import it.unisannio.paymentmanagementservice.model.PaymentType;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private int paymentId;

    private PaymentType type;

    private PaymentStatus status;

    private PaymentMethod paymentMethod;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    protected PaymentDetail() {}

    public PaymentDetail(int userId, int paymentId, BigDecimal amount) {
        this.userId = userId;
        this.paymentId = paymentId;
        this.amount = amount;
        this.type = null;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
