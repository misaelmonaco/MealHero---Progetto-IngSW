package it.unisannio.orderservice.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(nullable = false)
    private int reservationId;

    @Column(nullable = false)
    boolean served;

    protected Order() {}

    public Order(int  reservationId) {
        this.reservationId = reservationId;
        this.served = false;
    }

    public int getId() {
        return id;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int prenotationId) {
        this.reservationId = prenotationId;
    }

    public void toggle(){
        this.served = !this.served;
    }
}
