package it.unisannio.reservationservice.service;

import it.unisannio.reservationservice.entity.Reservation;
import it.unisannio.reservationservice.repository.ReservationRepository;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public ResponseEntity addReservation(Reservation reservation) {
        Reservation res = reservationRepository.save(reservation);
        return ResponseEntity.ok().body("Prenotazione aggiunta con successo!\nId: " + res.getId());
    }

    //La date deve essere nel formato "yyyy-MM-dd" e time nel formato "HH:MM"
    public ResponseEntity addReservation(int userId, int restaurantId, int numOfGuests, String date, String time, boolean includeParkingSpot){
        LocalDate localDate = LocalDate.parse(date);
        LocalTime localTime = LocalTime.parse(time);

        ResponseEntity response = findTableId(restaurantId, numOfGuests, localDate, localTime);
        if(!(response.getStatusCode().is2xxSuccessful())){
            return ResponseEntity.status(response.getStatusCode()).body("Non è stato possibile trovare un tavolo a quell'ora e per quella data!");
        }
        int tableId = (int)response.getBody();
        if(tableId == 0)
            return ResponseEntity.status(response.getStatusCode()).body("Non è stato possibile trovare un tavolo a quell'ora e per quella data!");

        int parkingSpotId = 0;
        ParkingSpot spot = null;
        if(includeParkingSpot){
            try{
                ResponseEntity response1 = findParkingSpot(restaurantId, numOfGuests, localDate, localTime);
                spot = (ParkingSpot) response1.getBody();
                parkingSpotId = spot.getId();
            } catch (ClassCastException e){ }
        }
        if(parkingSpotId == 0){
            if(includeParkingSpot){
                Reservation newReservation = new Reservation(userId, numOfGuests, restaurantId, tableId, parkingSpotId, localDate, localTime, false);
                Reservation res = reservationRepository.save(newReservation);
                return ResponseEntity.ok().body("ATTENZIONE!!: La prenotazione è stata aggiunta con successo ma non è stato possibile trovare un posto auto!\nId: " + res.getId());
            } else{
                Reservation newReservation = new Reservation(userId, numOfGuests, restaurantId, tableId, parkingSpotId, localDate, localTime, includeParkingSpot);
                Reservation res = reservationRepository.save(newReservation);
                return ResponseEntity.ok().body("Prenotazione aggiunta con successo!\nId: " + res.getId());
            }
        }else{
            Reservation newReservation = new Reservation(userId, numOfGuests, restaurantId, tableId, parkingSpotId, localDate, localTime, includeParkingSpot);
            Reservation res = reservationRepository.save(newReservation);
            return ResponseEntity.ok().body("Prenotazione aggiunta con successo!\nId: " + res.getId());
        }
    }

    public ResponseEntity deleteReservation(int reservationId){
        reservationRepository.deleteById(reservationId);
        return ResponseEntity.ok().body("Prenotazione rimossa con successo!");
    }

    public ResponseEntity updateReservation(int reservationId, Reservation reservation){
        Reservation oldReservation = reservationRepository.findById(reservationId).orElse(null);
        if(oldReservation == null){
            return ResponseEntity.notFound().build();
        }

        oldReservation.setNumOfGuests(reservation.getNumOfGuests());
        ResponseEntity response = findTableId(reservation.getRestaurantId(), reservation.getNumOfGuests(), reservation.getDate(), reservation.getTime());
        if(!(response.getStatusCode().is2xxSuccessful())){
            return ResponseEntity.status(response.getStatusCode()).body("Non è stato possibile trovare un tavolo a quell'ora e per quella data!");
        }
        int tableId = (int)response.getBody();
        oldReservation.setTableId(tableId);

        int parkingSpotId = 0;

        oldReservation.setParkingSpotId(0);
        if(reservation.isIncludeParkingSpot()){
            oldReservation.toggleIncludeParkingSpot();
            ResponseEntity response1 = findParkingSpot(reservation.getRestaurantId(), reservation.getNumOfGuests(), reservation.getDate(), reservation.getTime());
            if(!(response1.getStatusCode().is2xxSuccessful())){
                return ResponseEntity.status(response1.getStatusCode()).body("Non è stato possibile trovare uno stallo auto a quell'ora e per quella data!");
            }
            ParkingSpot spot = (ParkingSpot) response1.getBody();
            parkingSpotId = spot.getId();
            oldReservation.setParkingSpotId(parkingSpotId);
        }

        oldReservation.setDate(reservation.getDate());
        oldReservation.setTime(reservation.getTime());
        reservationRepository.save(oldReservation);

        return ResponseEntity.status(HttpStatus.OK).body("Prenotazione modificata con successo!");
    }

    public Reservation getReservationById(int reservationId){
        return reservationRepository.findById(reservationId).orElse(null);
    }

    public Iterable<Reservation> getAllReservations(){
        return reservationRepository.findAll();
    }

    public Iterable<Reservation> findReservationsByUserId(int userId){
        return reservationRepository.findByUserId(userId);
    }

    public Iterable<Reservation> findReservationsByRestaurantId(int restaurantId){
        return reservationRepository.findByRestaurantId(restaurantId);
    }

    public Iterable<Reservation> findReservationsByRestaurantAndDate(int restaurantId, LocalDate date){
        return reservationRepository.findByRestaurantIdAndDate(restaurantId, date);
    }

    public Iterable<Reservation> findReservationsByRestaurantAndDateAndTime(int restaurantId, LocalTime time, LocalDate date){
        return reservationRepository.findByRestaurantIdAndDateAndTime(restaurantId, date, time);
    }

    /**
     *  METODI PRIVATI AD USO DEL RESERVATION SERVICE
     */

    private ResponseEntity findTableId(int restaurantId, int numOfGuests, LocalDate date, LocalTime time) {
        Client client = ClientBuilder.newClient();
        WebTarget endpoint = client.target("http://localhost:8081");
        WebTarget resource = endpoint.path("/table/byRestaurant/" + restaurantId);

        //Ottieni la lista di tavoli
        List<RestaurantTable> tables = new ArrayList<>();
        try{
            tables = resource.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<RestaurantTable>>() {});
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Impossibile recuperare i tavoli!");
        }

        if(tables.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Non sono stati trovati tavoli!");

        //Ottieni la lista di prenotazioni per quello specifico ristorante
        List<Reservation> reservations = reservationRepository.findByRestaurantId(restaurantId);
        if(reservations == null)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        // Filtra le prenotazioni per la data e l'orario richiesti
        Iterator<Reservation> iter = reservations.iterator();
        while (iter.hasNext()) {
            Reservation reservation = iter.next();
            if (!(reservation.getDate().equals(date) && reservation.getTime().equals(time))) {
                iter.remove();  // Rimuovi solo se la prenotazione non coincide con la data e l'orario
            }
        }

        // Rimuovi i tavoli già occupati per quella specifica data e orario
        for (Reservation res : reservations) {
            if (res.getDate().equals(date) && res.getTime().equals(time)) {
                tables.removeIf(table -> table.getId() == res.getTableId());
            }
        }

        //Filtra i tavoli per assegnare quello più corretto
        RestaurantTable selectedTable = tables.stream()
                .filter(table -> table.getSeats() >= numOfGuests)
                .sorted(Comparator.comparingInt(RestaurantTable::getSeats))
                .findFirst()
                .orElse(null);

        if(selectedTable == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        client.close();
        return ResponseEntity.ok(selectedTable.getId());
    }

    private ResponseEntity findParkingSpot(int restaurantId, int numOfGuests, LocalDate date, LocalTime time) {
        Client client = ClientBuilder.newClient();
        WebTarget endpoint = client.target("http://localhost:8083");
        WebTarget resource = endpoint.path("/parkingArea/restaurant/" + restaurantId);

        // Inizializza la lista per i posti auto
        List<ParkingSpot> parkingSpots = new ArrayList<>();

        // Ottieni la lista delle aree di parcheggio
        List<ParkingArea> parkingAreas = new ArrayList<>();

        try{
            parkingAreas = resource.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ParkingArea>>() {});
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Impossibile trovare aree parcheggio!");
        }

        if(parkingAreas.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Non sono state trovate aree parcheggio!");

        // Per ogni area di parcheggio, recupera i posti auto
        for (ParkingArea parkingArea : parkingAreas) {
            // Ottieni i posti auto per questa area di parcheggio
            WebTarget parkingSpotsResource = endpoint.path("/parkingSpot/parkingArea/" + parkingArea.getId() );
            List<ParkingSpot> spots = parkingSpotsResource.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .get(new GenericType<List<ParkingSpot>>() {});

            if (spots != null) {
                parkingSpots.addAll(spots);
            }
        }

        // Ottieni la lista di prenotazioni per quello specifico ristorante
        List<Reservation> reservations = reservationRepository.findByRestaurantId(restaurantId);
        if (reservations == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Filtra le prenotazioni per data e ora
        reservations.removeIf(reservation -> !reservation.getDate().equals(date) || !reservation.getTime().equals(time));

        // Rimuove i posti auto occupati a quella data e ora
        for (Reservation res : reservations) {
            parkingSpots.removeIf(spot -> (spot.getId()) == (res.getParkingSpotId()));
        }

        if (parkingSpots.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nessun posto auto disponibile!");
        }

        // Se ci sono posti auto disponibili, restituisci il primo
        ParkingSpot selectedSpot = parkingSpots.get(0);

        client.close();
        return ResponseEntity.ok(selectedSpot);
    }

}

class RestaurantTable {
    private int id;
    private String name;
    private int restaurantId;
    private int seats;

    public RestaurantTable(String name, int restaurantId, int seats) {
        this.name = name;
        this.restaurantId = restaurantId;
        this.seats = seats;
    }

    public int getId() {
        return id;
    }

    public int getSeats() {
        return seats;
    }
}

class ParkingArea {
    private int id;
    private String name;
    private String address;
    private String description;
    private int restaurantId;

    public ParkingArea(String name, String address, String description, int restaurantId) {
        this.name = name;
        this.address = address;
        this.description = description;
        this.restaurantId = restaurantId;
    }

    public int getId() {
        return id;
    }
}

class ParkingSpot {
    private int id;
    private String number;
    private int parkingAreaId;

    public ParkingSpot(){}

    public ParkingSpot(int id, String number, int parkingAreaId) {
        this.id = id;
        this.number = number;
        this.parkingAreaId = parkingAreaId;
    }

    public int getId() {
        return this.id;
    }

    public int getParkingAreaId() {
        return parkingAreaId;
    }

    public String getNumber() {
        return number;
    }
}

