package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.entity.BankAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BankAccountRepository extends CrudRepository<BankAccount, Integer> {
   @Query("SELECT b FROM BankAccount b WHERE b.iban =:iban")
   Optional<BankAccount> findByIban(@Param("iban") String iban);

   @Query("SELECT b FROM BankAccount b WHERE b.restaurantId =:restaurantId")
   Optional<BankAccount> findByRestaurantId(@Param("restaurantId") Integer restaurantId);
}
