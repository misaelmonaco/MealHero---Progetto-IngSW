package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.entity.CreditCard;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreditCardRepository extends CrudRepository<CreditCard, Integer> {

    @Query("SELECT c FROM CreditCard c WHERE c.userId =: userId")
    Optional<CreditCard> findByUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM CreditCard c WHERE c.cardNumber =: cardNumber")
    Optional<CreditCard> getByCardNumber(@Param("cardNumber") String cardNumber);

    @Query("SELECT c FROM CreditCard c WHERE c.cardHolderName =: holderName")
    Iterable<CreditCard> getByHolderName(@Param("holderName") String holderName);
}
