package it.unisannio.restaurantmanagmentservice.repository;

import it.unisannio.restaurantmanagmentservice.entity.Restaurant;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r WHERE r.name LIKE %:restaurantName% AND r.location LIKE %:restaurantLocation%")
    Restaurant findByNameAndLocation(@Param("restaurantName") String name, @Param("restaurantLocation") String location);

    @Query("SELECT r FROM Restaurant r WHERE r.name LIKE %:restaurantName%")
    Iterable<Restaurant> findByName(@Param("restaurantName") String name);

    @Query("SELECT r FROM Restaurant r WHERE r.location LIKE %:restaurantLocation%")
    Iterable<Restaurant> findByLocation(@Param("restaurantLocation") String location);
}
