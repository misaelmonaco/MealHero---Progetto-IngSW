package it.unisannio.reservationservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private int userId;

    @Column(nullable = false)
    private int numOfGuests;

    @Column(nullable = false)
    private int restaurantId;

    @Column(nullable = false)
    private int tableId;

    @Column(nullable = false)
    private boolean includeParkingSpot;

    private int parkingSpotId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate date;

    @JsonFormat(pattern = "HH:mm:ss")
    @Column(nullable = false)
    private LocalTime time;

    protected Reservation() {}

    public Reservation(int userId, int numOfGuests, int restaurantId, int tableId, int parkingSpotId, LocalDate date, LocalTime time, boolean includeParkingSpot) {
        this.userId = userId;
        this.numOfGuests = numOfGuests;
        this.restaurantId = restaurantId;
        this.tableId = tableId;
        this.parkingSpotId = parkingSpotId;
        this.date = date;
        this.time = time;
        this.includeParkingSpot = includeParkingSpot;
    }

    public int getId() {
        return id;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public int getParkingSpotId() {
        return this.parkingSpotId;
    }

    public void setParkingSpotId(int parkingSpotId) {
        this.parkingSpotId = parkingSpotId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getNumOfGuests() {
        return numOfGuests;
    }

    public void setNumOfGuests(int numOfGuests) {
        this.numOfGuests = numOfGuests;
    }

    public boolean isIncludeParkingSpot() {
        return includeParkingSpot;
    }

    public void toggleIncludeParkingSpot() {
        includeParkingSpot = !includeParkingSpot;
    }
}
