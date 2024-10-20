package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.PaymentType;
import jakarta.persistence.Entity;

@Entity
public class PayPalTransaction extends Transaction {

    private int payPalAccountId;

    protected PayPalTransaction() {super.setPaymentMethod("PAYPAL");}

    public PayPalTransaction(double amount, String currency, int userId, int restaurantId, PaymentType paymentType, int payPalAccountId) {
        super(amount, currency, userId, restaurantId, paymentType);
        super.setPaymentMethod("PAYPAL");
        this.payPalAccountId = payPalAccountId;
    }

    public int getPayPalAccountId() {
        return payPalAccountId;
    }

    public void setPayPalAccountId(int payPalAccountId) {
        this.payPalAccountId = payPalAccountId;
    }
}
