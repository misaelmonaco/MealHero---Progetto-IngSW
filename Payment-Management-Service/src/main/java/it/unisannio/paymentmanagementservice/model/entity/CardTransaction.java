package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.PaymentMethod;
import jakarta.persistence.Entity;

@Entity
public class CardTransaction extends Transaction {

    private String creditCardNumber;

    protected CardTransaction() {}

    public CardTransaction(double amount, String currency, int userId, int restaurantId, String paymentType, String creditCardNumber) {
        super(amount, currency, userId, restaurantId, paymentType);
        super.setPaymentMethod(PaymentMethod.CARD);
        this.creditCardNumber = creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }
}
