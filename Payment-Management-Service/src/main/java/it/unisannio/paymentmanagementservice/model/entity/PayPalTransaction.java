package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.PaymentMethod;
import jakarta.persistence.Entity;

@Entity
public class PayPalTransaction extends Transaction {

    private int payPalAccountId;

    protected PayPalTransaction() {}

    public PayPalTransaction(double amount, String currency, int userId, int restaurantId, String paymentType, int transactionId) {
        super(amount, currency, userId, restaurantId, paymentType);
        super.setPaymentMethod(PaymentMethod.PAYPAL);
        this.payPalAccountId = transactionId;
    }

    public int getPayPalAccountId() {
        return payPalAccountId;
    }

    public void setPayPalAccountId(int payPalAccountId) {
        this.payPalAccountId = payPalAccountId;
    }
}
