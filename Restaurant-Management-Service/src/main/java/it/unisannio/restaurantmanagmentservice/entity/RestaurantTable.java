package it.unisannio.restaurantmanagmentservice.entity;

import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private int restaurantId;

    @NotNull
    private int seats;

    protected RestaurantTable() {}

    public RestaurantTable(String name, int restaurantId, int seats) {
        this.name = name;
        this.restaurantId = restaurantId;
        this.seats = seats;
    }

    public int getId() {
        return id;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
