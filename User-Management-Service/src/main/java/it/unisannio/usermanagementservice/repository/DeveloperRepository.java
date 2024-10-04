package it.unisannio.usermanagementservice.repository;

import it.unisannio.usermanagementservice.entity.Developer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeveloperRepository extends CrudRepository<Developer, Integer> {
    @Query
    Developer findByUsername(String username);

    @Query
    Developer findByEmail(String email);
}
