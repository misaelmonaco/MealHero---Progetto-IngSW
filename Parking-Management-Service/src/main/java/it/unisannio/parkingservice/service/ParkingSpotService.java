package it.unisannio.parkingservice.service;

import it.unisannio.parkingservice.entity.ParkingSpot;
import it.unisannio.parkingservice.repository.ParkingAreaRepository;
import it.unisannio.parkingservice.repository.ParkingSpotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSpotService {

    @Autowired
    private ParkingSpotRepository parkingSpotRepository;
    @Autowired
    private ParkingAreaRepository parkingAreaRepository;

    public ResponseEntity addParkingSpot(ParkingSpot parkingSpot) {
        parkingSpotRepository.save(parkingSpot);
        return ResponseEntity.status(HttpStatus.CREATED).body("Posto Auto aggiunto con successo!");
    }

    public ResponseEntity deleteParkingSpot(int parkingSpotId) {
        parkingSpotRepository.deleteById(parkingSpotId);
        return ResponseEntity.status(HttpStatus.OK).body("Posto Auto rimosso con successo!");
    }

    public ResponseEntity addToParkingArea(int pakingAreaid, List<ParkingSpot> parkingSpots) {
        if(!(parkingAreaRepository.findById(pakingAreaid).isPresent())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Parking Area non trovata!");
        }
        parkingSpotRepository.saveAll(parkingSpots);
        return ResponseEntity.status(HttpStatus.OK).body("Posto/i Auto aggiunto/i con successo!");
    }

    public ResponseEntity updateParkingSpot(ParkingSpot parkingSpot, int parkingSpotId) {
        ParkingSpot oldParkingSpot = parkingSpotRepository.findById(parkingSpotId).orElse(null);
        if (oldParkingSpot == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Posto Auto non trovato!");
        }

        oldParkingSpot.setParkingAreaId(parkingSpot.getParkingAreaId());
        oldParkingSpot.setNumber(parkingSpot.getNumber());
        parkingSpotRepository.save(oldParkingSpot);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ParkingSpot getParkingSpotById(int parkingSpotId) {
        return parkingSpotRepository.findById(parkingSpotId).orElse(null);
    }

    public Iterable<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAll();
    }

    public List<ParkingSpot> getParkingSpotsByParkingAreaId(int parkingAreaId) {
        return parkingSpotRepository.findByParkingAreaId(parkingAreaId);
    }
}
