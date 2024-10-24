package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.AESEncryption;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Base64;

@Entity
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private int restaurantId;

    @Column(nullable = false, unique = true)
    private String iban;

    @Column(nullable = false)
    private String accountHolderName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal balance;

    public BankAccount() {}

    public BankAccount(int restaurantId, String iban, String accountHolderName) throws Exception {
        this.restaurantId = restaurantId;
        this.iban = AESEncryption.encrypt(Base64.getEncoder().encodeToString(iban.getBytes()));
        this.accountHolderName = AESEncryption.encrypt(Base64.getEncoder().encodeToString(accountHolderName.getBytes()));
        this.balance = BigDecimal.ZERO;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIban() throws Exception {
        return new String(Base64.getDecoder().decode(AESEncryption.decrypt(this.iban)));
    }

    public void setIban(String iban) throws Exception {
        this.iban = AESEncryption.encrypt(Base64.getEncoder().encodeToString(iban.getBytes()));
    }

    public String getAccountHolderName() throws Exception {
        return new String(Base64.getDecoder().decode(AESEncryption.decrypt(this.accountHolderName)));
    }

    public void setAccountHolderName(String accountHolderName) throws Exception {
        this.accountHolderName = AESEncryption.encrypt(Base64.getEncoder().encodeToString(accountHolderName.getBytes()));
    }

    public BigDecimal getBalance() throws Exception {
        return this.balance;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setBalance(BigDecimal balance) throws Exception {
        this.balance = balance;
    }

    public boolean hasSufficientBalance(BigDecimal amount) throws Exception {
        return this.balance.compareTo(amount) >= 0;
    }

    public void charge(BigDecimal amount) throws Exception {
        if (hasSufficientBalance(amount)) {
            setBalance(this.balance.subtract(amount));
            System.out.println("Addebitato " + amount + " dal conto bancario. Nuovo saldo: " + this.balance);
        } else {
            throw new IllegalArgumentException("Saldo bancario insufficiente.");
        }
    }
}
