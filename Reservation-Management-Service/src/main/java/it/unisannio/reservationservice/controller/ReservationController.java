package it.unisannio.reservationservice.controller;

import it.unisannio.reservationservice.entity.Reservation;
import it.unisannio.reservationservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/add")
    public ResponseEntity addReservation(@RequestBody Reservation reservation) {
        return reservationService.addReservation(reservation);
    }

    @PostMapping("/add/{userId}/{restaurantId}/{numOfGuests}/{date}/{time}/{parkingSpot}")
    public ResponseEntity addReservation(@PathVariable int userId, @PathVariable int restaurantId, @PathVariable int numOfGuests, @PathVariable String date, @PathVariable String time, @PathVariable boolean parkingSpot) {
        return reservationService.addReservation(userId, restaurantId, numOfGuests, date, time, parkingSpot);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteReservation(@PathVariable int id) {
        if(reservationService.getReservationById(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Prenotazione con id " + id + " non trovata!");
        }
        return reservationService.deleteReservation(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateReservation(@PathVariable int id, @RequestBody Reservation reservation) {
        return reservationService.updateReservation(id, reservation);
    }

    @GetMapping("/{id}")
    public ResponseEntity getReservation(@PathVariable int id) {
        if(reservationService.getReservationById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(reservationService.getReservationById(id));
    }
    @GetMapping("/all")
    public ResponseEntity getAllReservations() {
        if(!(reservationService.getAllReservations().iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(reservationService.getAllReservations());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getReservationByUserId(@PathVariable int id) {
        if(!(reservationService.findReservationsByUserId(id).iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(reservationService.findReservationsByUserId(id));
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity getReservationByRestaurantId(@PathVariable int id) {
        if(!(reservationService.findReservationsByRestaurantId(id).iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(reservationService.findReservationsByRestaurantId(id));
    }

    @GetMapping("/restaurant/{id}/date/{date}")
    public ResponseEntity getReservationByDateAndRestaurant(@PathVariable int id, @PathVariable String date) {
        if (id <= 0)
            return ResponseEntity.badRequest().body("Restaurant ID non valido!.");
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            if (!(reservationService.findReservationsByRestaurantAndDate(id, parsedDate).iterator().hasNext()))
                return ResponseEntity.notFound().build();
            return ResponseEntity.ok().body(reservationService.findReservationsByRestaurantAndDate(id, parsedDate));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("La data ha un formato sbagliato! Il formato corretto è YYYY-MM-DD.");
        }
    }

    @GetMapping("/restaurant/{id}/date/{date}/time/{time}")
    public ResponseEntity getReservationByDateTimeRestaurant(@PathVariable int id, @PathVariable String date, @PathVariable String time) {
        if (id <= 0) {
            return ResponseEntity.badRequest().body("Restaurant ID non valido!.");
        }
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            LocalTime parsedTime = LocalTime.parse(time);
            if (!reservationService.findReservationsByRestaurantAndDateAndTime(id, parsedTime, parsedDate).iterator().hasNext()) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok().body(reservationService.findReservationsByRestaurantAndDateAndTime(id, parsedTime, parsedDate));
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("La data o l'orario hanno un formato sbagliato! La data deve essere YYYY-MM-DD e l'orario HH:mm.");
        }
    }
}
