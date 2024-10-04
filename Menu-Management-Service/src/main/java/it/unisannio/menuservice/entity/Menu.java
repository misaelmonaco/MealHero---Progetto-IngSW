package it.unisannio.menuservice.entity;

import jakarta.persistence.*;


@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private int restaurantId;

    protected Menu() {}

    public Menu(String name, String description, int restaurantId) {
        this.name = name;
        this.description = description;
        this.restaurantId = restaurantId;
    }

    public int getId() {
        return id;
    }

    public void setRestaurantId(int id) {
        this.restaurantId = id;
    }

    public int getRestaurantId() {
        return restaurantId;
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
}
