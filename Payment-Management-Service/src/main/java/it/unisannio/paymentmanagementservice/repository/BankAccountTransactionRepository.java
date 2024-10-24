package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.model.entity.BankAccountTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountTransactionRepository extends CrudRepository<BankAccountTransaction, Integer> {

    @Query("SELECT t FROM BankAccountTransaction t WHERE t.bankAccountReceiverId =:restaurantId")
    Iterable<BankAccountTransaction> findByRestaurantId(@Param("restaurantId") Integer restaurantId);
}
