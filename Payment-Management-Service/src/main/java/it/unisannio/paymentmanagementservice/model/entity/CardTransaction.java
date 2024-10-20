package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.PaymentType;
import jakarta.persistence.Entity;

@Entity
public class CardTransaction extends Transaction {

    private String creditCardNumber;

    protected CardTransaction() { super(); super.setPaymentMethod("CARD");}

    public CardTransaction(double amount, String currency, int userId, int restaurantId, PaymentType paymentType, String creditCardNumber) {
        super(amount, currency, userId, restaurantId, paymentType);
        this.setPaymentMethod("CARD");
        this.creditCardNumber = creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }
}
