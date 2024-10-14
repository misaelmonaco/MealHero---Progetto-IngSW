package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.entity.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends CrudRepository<Payment, Integer> {

    @Query("SELECT p FROM Payment p WHERE p.restaurantPayeeId =: restaurantId")
    Iterable<Payment> getPaymentsByRestaurant(@Param("restaurantId") int restaurantId);

    @Query("SELECT p FROM Payment p WHERE p.reservationID =: reservationId")
    Payment getPaymentByReservation(@Param("reservationId") int reservationId);
}
