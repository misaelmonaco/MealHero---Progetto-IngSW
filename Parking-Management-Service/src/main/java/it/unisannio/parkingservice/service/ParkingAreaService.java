package it.unisannio.parkingservice.service;

import it.unisannio.parkingservice.entity.ParkingArea;
import it.unisannio.parkingservice.repository.ParkingAreaRepository;
import it.unisannio.parkingservice.repository.ParkingSpotRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ParkingAreaService {

    @Autowired
    private ParkingAreaRepository parkingAreaRepository;
    @Autowired
    private ParkingSpotRepository parkingSpotRepository;

    public ResponseEntity addParkingArea(ParkingArea parkingArea) {
        parkingAreaRepository.save(parkingArea);
        return ResponseEntity.status(HttpStatus.CREATED).body("Area parcheggio aggiunta con successo!");
    }

    @Transactional
    public ResponseEntity deleteParkingArea(int id) {
        parkingSpotRepository.deleteByParkingAreaId(id);
        parkingAreaRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Area parcheggio eliminata con successo!");
    }

    public ResponseEntity updateParkingArea(ParkingArea parkingArea, int id) {
        ParkingArea oldParkingArea = parkingAreaRepository.findById(id).orElse(null);

        if(oldParkingArea == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Area parcheggio non trovata!");

        oldParkingArea.setName(parkingArea.getName());
        oldParkingArea.setDescription(parkingArea.getDescription());
        oldParkingArea.setAddress(parkingArea.getAddress());
        oldParkingArea.setRestaurantId(parkingArea.getRestaurantId());
        parkingAreaRepository.save(oldParkingArea);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ParkingArea getParkingArea(int id) {
        return parkingAreaRepository.findById(id).orElse(null);
    }

    public Iterable<ParkingArea> getAllParkingAreas() {
        return parkingAreaRepository.findAll();
    }

    public Iterable<ParkingArea> getParkingAreasByRestaurantId(int id) {
        return parkingAreaRepository.findByRestaurantId(id);
    }
}
