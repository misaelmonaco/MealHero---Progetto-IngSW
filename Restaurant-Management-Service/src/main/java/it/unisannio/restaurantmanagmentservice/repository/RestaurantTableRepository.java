package it.unisannio.restaurantmanagmentservice.repository;

import it.unisannio.restaurantmanagmentservice.entity.RestaurantTable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;



public interface RestaurantTableRepository extends CrudRepository<RestaurantTable, Integer> {
   @Query("SELECT t FROM RestaurantTable t WHERE t.restaurantId = :restaurantId")
   Iterable<RestaurantTable> findByRestaurantId(@Param("restaurantId") int restaurantId);

   @Modifying
   @Query("DELETE FROM RestaurantTable rt WHERE rt.restaurantId =:restaurantId")
    void deleteByRestaurantId(@Param("restaurantId") int restaurantId);
}
