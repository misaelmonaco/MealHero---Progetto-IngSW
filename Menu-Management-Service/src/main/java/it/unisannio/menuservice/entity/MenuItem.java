package it.unisannio.menuservice.entity;

import jakarta.persistence.*;

@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private double price;

    private boolean available;

    private int menuId;

    protected MenuItem(){this.available = true;}

    public MenuItem(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = true;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void toogleAvailable() {
        this.available = !available;
    }
}
