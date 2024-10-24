package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.model.entity.CardTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTransactionRepository extends CrudRepository<CardTransaction, Integer> {
}
