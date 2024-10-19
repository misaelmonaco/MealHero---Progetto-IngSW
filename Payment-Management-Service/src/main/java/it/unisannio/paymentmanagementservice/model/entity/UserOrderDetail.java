package it.unisannio.paymentmanagementservice.model.entity;

import jakarta.persistence.*;

@Entity
public class UserOrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private int orderId;

    @Column(nullable = false)
    private int menuItemId;

    @Column(nullable = false)
    private int quantity;

    protected UserOrderDetail() {}

    public UserOrderDetail(int userId, int orderId, int menuItemId, int quantity) {
        this.userId = userId;
        this.orderId = orderId;
        this.menuItemId = menuItemId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}