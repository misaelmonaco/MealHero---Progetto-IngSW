package it.unisannio.paymentmanagementservice.repository;

import it.unisannio.paymentmanagementservice.model.entity.CashTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashTransactionRepository extends CrudRepository<CashTransaction, Integer> {
}
