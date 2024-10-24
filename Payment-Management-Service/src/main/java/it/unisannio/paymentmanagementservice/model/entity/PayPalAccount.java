package it.unisannio.paymentmanagementservice.model.entity;

import it.unisannio.paymentmanagementservice.model.AESEncryption;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Base64;

@Entity
public class PayPalAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false, unique = true)
    private int userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, scale = 2, precision = 10)
    private BigDecimal balance;

    public PayPalAccount() {}

    public PayPalAccount(int userId, String email, String password, BigDecimal balance) throws Exception {
        this.userId = userId;
        this.email = AESEncryption.encrypt(Base64.getEncoder().encodeToString(email.getBytes()));;
        this.password = AESEncryption.encrypt(Base64.getEncoder().encodeToString(password.getBytes()));
        this.balance = balance;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() throws Exception {
        return new String(Base64.getDecoder().decode(AESEncryption.decrypt(this.email)));
    }

    public void setEmail(String email) throws Exception {
        this.email = AESEncryption.encrypt(Base64.getEncoder().encodeToString(email.getBytes()));
    }

    public String getPassword() throws Exception {
        return new String(Base64.getDecoder().decode(AESEncryption.decrypt(this.password)));
    }

    public void setPassword(String password) throws Exception {
        this.password = AESEncryption.encrypt(Base64.getEncoder().encodeToString(password.getBytes()));
    }

    public BigDecimal getBalance() throws Exception {
        return this.balance;
    }

    public int getUserId() {
        return userId;
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
        } else {
            throw new IllegalArgumentException("Saldo PayPal insufficiente");
        }
    }
}
