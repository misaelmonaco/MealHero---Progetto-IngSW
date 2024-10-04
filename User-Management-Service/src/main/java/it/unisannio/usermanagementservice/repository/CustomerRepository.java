package it.unisannio.usermanagementservice.repository;

import it.unisannio.usermanagementservice.entity.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    @Query
    Customer findByUsername(String username);

    @Query
    Customer findByEmail(String email);
}
