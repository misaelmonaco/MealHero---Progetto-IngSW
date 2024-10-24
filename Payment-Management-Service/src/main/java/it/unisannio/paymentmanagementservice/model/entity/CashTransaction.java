package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.PaymentType;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

@Entity
public class CashTransaction extends Transaction {

    private int employeeId;

    protected CashTransaction() {super.setPaymentMethod("CASH");}

    public CashTransaction(BigDecimal amount, String currency, int userId, int restaurantId, PaymentType paymentType, int employeeId) {
        super(amount, currency, userId, restaurantId, paymentType);
        super.setPaymentMethod("CASH");
        this.employeeId = employeeId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
