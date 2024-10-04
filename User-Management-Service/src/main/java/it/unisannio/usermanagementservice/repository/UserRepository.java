package it.unisannio.usermanagementservice.repository;

import it.unisannio.usermanagementservice.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<Users, Integer> {

    @Query
    Users findByUsername(String username);

    @Query
    Iterable<Users> findByRole(String role);
}
