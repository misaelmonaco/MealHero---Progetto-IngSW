package it.unisannio.parkingservice.repository;

import it.unisannio.parkingservice.entity.ParkingArea;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingAreaRepository extends CrudRepository<ParkingArea, Integer> {

    @Query("SELECT pa FROM ParkingArea pa WHERE pa.restaurantId =:restaurantId")
    Iterable<ParkingArea> findByRestaurantId(@Param("restaurantId") Integer restaurantId);
}
