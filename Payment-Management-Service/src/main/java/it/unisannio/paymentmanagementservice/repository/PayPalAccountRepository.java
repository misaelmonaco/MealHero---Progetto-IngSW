package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.entity.PayPalAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PayPalAccountRepository extends CrudRepository<PayPalAccount, Integer> {

    @Query("SELECT pa FROM PayPalAccount pa WHERE pa.userId =: userId")
    Optional<PayPalAccount> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT pa FROM PayPalAccount pa WHERE pa.email =: email")
    Optional<PayPalAccount> findByEmail(@Param("email") String email);
}
