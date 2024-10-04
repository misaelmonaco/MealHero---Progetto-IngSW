package it.unisannio.parkingservice.repository;

import it.unisannio.parkingservice.entity.ParkingSpot;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpotRepository extends CrudRepository<ParkingSpot, Integer> {

    @Query("SELECT ps FROM ParkingSpot ps WHERE ps.parkingAreaId =:parkingId")
    List<ParkingSpot> findByParkingAreaId(int parkingId);

    @Modifying
    @Query("DELETE FROM ParkingSpot p WHERE p.parkingAreaId = :parkingAreaId")
    void deleteByParkingAreaId(@Param("parkingAreaId") int parkingAreaId);
}
