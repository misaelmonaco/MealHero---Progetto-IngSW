package it.unisannio.menuservice.repository;

import it.unisannio.menuservice.entity.Menu;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Integer> {

    @Query("SELECT m FROM Menu m WHERE m.restaurantId =:restaurantId ")
    Optional<List<Menu>> findByRestaurantId(int restaurantId);
}
