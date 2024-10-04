package it.unisannio.restaurantmanagmentservice.controller;

import it.unisannio.restaurantmanagmentservice.entity.Restaurant;
import it.unisannio.restaurantmanagmentservice.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/add")
    public ResponseEntity addRestaurant(@RequestBody Restaurant restaurant) {
        return restaurantService.addRestaurant(restaurant);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRestaurant(@PathVariable int id) {
        if(restaurantService.getRestaurantById(id) == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return restaurantService.deleteRestaurant(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateRestaurant(@RequestBody Restaurant restaurant, @PathVariable int id) {
        return restaurantService.updateRestaurant(restaurant, id);
    }

    @GetMapping("/all")
    public ResponseEntity getAllRestaurants() {
        if(!restaurantService.getAllRestaurants().iterator().hasNext())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lista di ristoranti non trovata!");

        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getAllRestaurants());
    }

    @GetMapping("/{id}")
    public ResponseEntity getRestaurant(@PathVariable int id) {
        if(restaurantService.getRestaurantById(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ristorante non trovato!");
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity getRestaurantByName(@PathVariable String name) {
        if(!restaurantService.getRestaurantByName(name).iterator().hasNext())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ristorante/i non trovato/i!");
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantByName(name));
    }

    @GetMapping("/location/{location}")
    public ResponseEntity getRestaurantByLocation(@PathVariable String location) {
        if(!restaurantService.getRestaurantByLocation(location).iterator().hasNext())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ristorante/i non trovato/i!");
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantByLocation(location));
    }

    @GetMapping("/{location}/{name}")
    public ResponseEntity getRestaurantByNameAndLocation(@PathVariable String location, @PathVariable String name) {
        if(restaurantService.getRestaurantByNameAndLocation(name, location) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ristorante non trovato!");
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantByNameAndLocation(name, location));
    }
}
