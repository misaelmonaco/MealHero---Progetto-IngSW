package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.PaymentMethod;
import jakarta.persistence.Entity;

@Entity
public class CashTransaction extends Transaction {

    private int employeeId;

    protected CashTransaction() {}

    public CashTransaction(double amount, String currency, int userId, int restaurantId, String paymentType, int employeeId) {
        super(amount, currency, userId, restaurantId, paymentType);
        super.setPaymentMethod(PaymentMethod.CASH);
        this.employeeId = employeeId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }
}
