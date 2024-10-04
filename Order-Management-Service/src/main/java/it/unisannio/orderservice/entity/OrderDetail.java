package it.unisannio.orderservice.entity;

import jakarta.persistence.*;

@Entity
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private int orderId;

    @Column(nullable = false)
    private int menuItemId;

    @Column(nullable = false)
    private int quantity;

    protected OrderDetail() {}

    public OrderDetail(int menuItemId, int quantity) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderId = 0;
    }

    public OrderDetail(int menuItemId, int quantity, int orderId) {
        this.menuItemId = menuItemId;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(int menuItemId) {
        this.menuItemId = menuItemId;
    }

    public int getOrderId() { return orderId; }

    public void setOrderId(int orderId) { this.orderId = orderId; }
}
