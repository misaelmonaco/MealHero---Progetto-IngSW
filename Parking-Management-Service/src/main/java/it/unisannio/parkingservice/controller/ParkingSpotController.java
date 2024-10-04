package it.unisannio.parkingservice.controller;

import it.unisannio.parkingservice.entity.ParkingSpot;
import it.unisannio.parkingservice.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parkingSpot")
public class ParkingSpotController {

    @Autowired
    private ParkingSpotService parkingSpotService;

    @PostMapping("/add")
    public ResponseEntity addParkingSpot(@RequestBody ParkingSpot parkingSpot) {
        return parkingSpotService.addParkingSpot(parkingSpot);
    }

    @PostMapping("/addToParkingArea/{id}")
    public ResponseEntity addToParkingArea(@PathVariable int id, @RequestBody List<ParkingSpot> parkingSpots) {
        return parkingSpotService.addToParkingArea(id, parkingSpots);
    }

    @DeleteMapping("/delete/{parkingSpotId}")
    public ResponseEntity deleteParkingSpot(@PathVariable int parkingSpotId) {
        if(parkingSpotService.getParkingSpotById(parkingSpotId) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return parkingSpotService.deleteParkingSpot(parkingSpotId);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateParkingSpot(@PathVariable int id, @RequestBody ParkingSpot parkingSpot) {
        return parkingSpotService.updateParkingSpot(parkingSpot, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getParkingSpot(@PathVariable int id) {
        if(parkingSpotService.getParkingSpotById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stallo Auto non trovato!");
        }
        return ResponseEntity.ok(parkingSpotService.getParkingSpotById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllParkingSpots() {
        if(!(parkingSpotService.getAllParkingSpots().iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stalli Auto non trovati!");
        return ResponseEntity.ok(parkingSpotService.getAllParkingSpots());
    }

    @GetMapping("/parkingArea/{id}")
    public ResponseEntity getAllByParkingArea (@PathVariable int id) {
        if(!(parkingSpotService.getParkingSpotsByParkingAreaId(id).iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stalli Auto non trovati!");
        return ResponseEntity.ok(parkingSpotService.getParkingSpotsByParkingAreaId(id));
    }
}
