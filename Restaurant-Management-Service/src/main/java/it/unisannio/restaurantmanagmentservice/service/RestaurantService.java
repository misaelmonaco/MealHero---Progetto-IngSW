package it.unisannio.restaurantmanagmentservice.service;

import it.unisannio.restaurantmanagmentservice.entity.Restaurant;
import it.unisannio.restaurantmanagmentservice.repository.RestaurantRepository;
import it.unisannio.restaurantmanagmentservice.repository.RestaurantTableRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    public ResponseEntity addRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ristorante aggiunto con successo");
    }

    @Transactional
    public ResponseEntity deleteRestaurant(int id) {
        restaurantTableRepository.deleteByRestaurantId(id);
        restaurantRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity updateRestaurant(Restaurant restaurant, int id) {
        Restaurant existingRestaurant = restaurantRepository.findById(id).orElse(null);
        if (existingRestaurant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        existingRestaurant.setName(restaurant.getName());
        existingRestaurant.setAddress(restaurant.getAddress());
        existingRestaurant.setLocation(restaurant.getLocation());
        existingRestaurant.setPhone(restaurant.getPhone());
        existingRestaurant.setEmail(restaurant.getEmail());
        restaurantRepository.save(existingRestaurant);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Integer id) {
        return restaurantRepository.findById(id).orElse(null);
    }

    public Iterable<Restaurant> getRestaurantByName(String name) {
        return restaurantRepository.findByName(name);
    }

    public Iterable<Restaurant> getRestaurantByLocation(String location) {
        return restaurantRepository.findByLocation(location);
    }

    public Restaurant getRestaurantByNameAndLocation(String name, String location) {
        return restaurantRepository.findByNameAndLocation(name, location);
    }
}
