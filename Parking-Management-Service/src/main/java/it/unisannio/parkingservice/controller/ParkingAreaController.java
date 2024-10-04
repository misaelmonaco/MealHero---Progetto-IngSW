package it.unisannio.parkingservice.controller;

import it.unisannio.parkingservice.entity.ParkingArea;
import it.unisannio.parkingservice.service.ParkingAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parkingArea")
public class ParkingAreaController {

    @Autowired
    private ParkingAreaService parkingAreaService;

    @PostMapping("/add")
    public ResponseEntity addParkingArea(@RequestBody ParkingArea parkingArea) {
        return parkingAreaService.addParkingArea(parkingArea);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteParkingArea(@PathVariable int id) {
        if(parkingAreaService.getParkingArea(id) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return parkingAreaService.deleteParkingArea(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateParkingArea(@PathVariable int id, @RequestBody ParkingArea parkingArea) {
        return parkingAreaService.updateParkingArea(parkingArea, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getParkingArea(@PathVariable int id) {
        if(parkingAreaService.getParkingArea(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Area Parcheggio non trovata!");
        }
       return ResponseEntity.status(HttpStatus.OK).body(parkingAreaService.getParkingArea(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllParkingAreas() {
        if(!(parkingAreaService.getAllParkingAreas().iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aree Parcheggio non trovate!");
        return ResponseEntity.status(HttpStatus.OK).body(parkingAreaService.getAllParkingAreas());
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity getRestaurantParkingAreas(@PathVariable int id) {
        if(!(parkingAreaService.getParkingAreasByRestaurantId(id).iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aree Parcheggio non trovate!");
        return ResponseEntity.status(HttpStatus.OK).body(parkingAreaService.getParkingAreasByRestaurantId(id));
    }
}
