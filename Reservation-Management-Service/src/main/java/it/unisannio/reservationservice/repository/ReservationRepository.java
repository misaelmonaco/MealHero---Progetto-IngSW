package it.unisannio.reservationservice.repository;

import it.unisannio.reservationservice.entity.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

    @Query("SELECT r FROM Reservation r WHERE r.restaurantId =:restaurantId AND r.date =:date AND r.time =:time")
    public List<Reservation> findByRestaurantIdAndDateAndTime(Integer restaurantId, LocalDate date, LocalTime time);

    @Query("SELECT r FROM Reservation r WHERE r.restaurantId =:restaurantId AND r.date =:date")
    public List<Reservation> findByRestaurantIdAndDate(Integer restaurantId, LocalDate date);

    @Query("SELECT r FROM Reservation r WHERE r.restaurantId =:restaurantId")
    public List<Reservation> findByRestaurantId(Integer restaurantId);

    @Query("SELECT r FROM Reservation r WHERE r.userId =:userId")
    public List<Reservation> findByUserId(Integer userId);
}
