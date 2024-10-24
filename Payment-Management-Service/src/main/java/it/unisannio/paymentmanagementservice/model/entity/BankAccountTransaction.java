package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.Currency;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class BankAccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int bankAccountSenderId;

    private int bankAccountReceiverId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    private LocalDateTime transactionDate;

    private Currency currency;

    protected BankAccountTransaction() { this.transactionDate = LocalDateTime.now();}

    public BankAccountTransaction(int bankAccountSenderId, int bankAccountReceiverId, BigDecimal amount, String currency) {
        this.bankAccountSenderId = bankAccountSenderId;
        this.bankAccountReceiverId = bankAccountReceiverId;
        this.amount = amount;
        this.currency = Currency.valueOf(currency);
        this.transactionDate = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getBankAccountReceiverId() {
        return bankAccountReceiverId;
    }

    public void setBankAccountReceiverId(int bankAccountReceiverId) {
        this.bankAccountReceiverId = bankAccountReceiverId;
    }

    public int getBankAccountSenderId() {
        return bankAccountSenderId;
    }

    public void setBankAccountSenderId(int bankAccountSenderId) {
        this.bankAccountSenderId = bankAccountSenderId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
