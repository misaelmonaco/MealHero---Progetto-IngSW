package it.unisannio.restaurantmanagmentservice.service;

import it.unisannio.restaurantmanagmentservice.entity.RestaurantTable;
import it.unisannio.restaurantmanagmentservice.repository.RestaurantTableRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantTableService {

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    public ResponseEntity addRestaurantTable(RestaurantTable restaurantTable){
        restaurantTableRepository.save(restaurantTable);
        return ResponseEntity.status(HttpStatus.CREATED).body("Tavolo aggiunto con successo");
    }

    public ResponseEntity deleteRestaurantTable(int id){
        restaurantTableRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    public ResponseEntity updateRestaurantTable(RestaurantTable restaurantTable, int id){
         RestaurantTable existingRestaurantTable = restaurantTableRepository.findById(id).orElse(null);
         if(existingRestaurantTable==null){
             return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
         }

         existingRestaurantTable.setName(restaurantTable.getName());
         existingRestaurantTable.setSeats(restaurantTable.getSeats());
         restaurantTableRepository.save(existingRestaurantTable);
         return ResponseEntity.status(HttpStatus.OK).build();
    }

    public RestaurantTable getRestaurantTable(int id){
        return restaurantTableRepository.findById(id)
                .orElse(null);
    }

    public Iterable<RestaurantTable> getAllRestaurantTables(int restaurantId){
        return restaurantTableRepository.findByRestaurantId(restaurantId);
    }

    public Iterable<RestaurantTable> getAllRestaurantTables(){
        return restaurantTableRepository.findAll();
    }
}
