package it.unisannio.orderservice.repository;

import it.unisannio.orderservice.entity.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {

    @Query("SELECT o FROM Order o WHERE o.reservationId = :reservationId")
    Iterable<Order> findByReservationId(@Param("reservationId") int reservationId);
}

