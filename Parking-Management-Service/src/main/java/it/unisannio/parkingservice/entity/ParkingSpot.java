package it.unisannio.parkingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private String number;

    @NotNull
    private int parkingAreaId;

    protected ParkingSpot() {}

    public ParkingSpot(String number, int parkingAreaId) {
        this.number = number;
        this.parkingAreaId = parkingAreaId;
    }

    public int getParkingAreaId() {
        return parkingAreaId;
    }

    public void setParkingAreaId(int parkingAreaId) {
        this.parkingAreaId = parkingAreaId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }
}
