package it.unisannio.usermanagementservice.repository;

import it.unisannio.usermanagementservice.entity.Admin;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends CrudRepository<Admin, Integer> {
    @Query
    Admin findByUsername(String username);

    @Query("SELECT a FROM Admin a WHERE a.restaurantId = :id")
    Iterable<Admin> findByRestaurantId(@Param("id") Integer id);

    @Query
    Admin findByEmail(String email);

}
