package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.model.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Integer> {

    @Query("SELECT t FROM Transaction  t WHERE t.userId =:userId")
    Iterable<Transaction> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT t FROM Transaction  t WHERE t.restaurantId =:restaurantId")
    Iterable<Transaction> findByRestaurantId(@Param("restaurantId") Integer restaurantId);
}
