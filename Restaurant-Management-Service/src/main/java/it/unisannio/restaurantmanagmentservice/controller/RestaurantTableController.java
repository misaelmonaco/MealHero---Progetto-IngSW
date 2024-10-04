package it.unisannio.restaurantmanagmentservice.controller;

import it.unisannio.restaurantmanagmentservice.entity.RestaurantTable;
import it.unisannio.restaurantmanagmentservice.repository.RestaurantTableRepository;
import it.unisannio.restaurantmanagmentservice.service.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/table")
public class RestaurantTableController {

    @Autowired
    private RestaurantTableService restaurantTableService;
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @PostMapping("/add")
    public ResponseEntity addRestaurantTable(@RequestBody RestaurantTable restaurantTable) {
        return restaurantTableService.addRestaurantTable(restaurantTable);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRestaurantTable(@PathVariable int id) {
        if(!(restaurantTableRepository.findById(id).isPresent())) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return restaurantTableService.deleteRestaurantTable(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateRestaurantTable(@RequestBody RestaurantTable restaurantTable, @PathVariable int id) {
        return restaurantTableService.updateRestaurantTable(restaurantTable, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getRestaurantTable(@PathVariable int id) {
        if(restaurantTableService.getRestaurantTable(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tavolo non trovato!");
        return ResponseEntity.ok().body(restaurantTableService.getRestaurantTable(id));
    }

    @GetMapping("/byRestaurant/{id}")
    public ResponseEntity getAllRestaurantTablesByRestaurant(@PathVariable int id) {
        if(!(restaurantTableService.getAllRestaurantTables(id).iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tavoli non trovati!");
        return ResponseEntity.ok().body(restaurantTableService.getAllRestaurantTables(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllRestaurantTables() {
        if(!(restaurantTableService.getAllRestaurantTables().iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tavoli non trovati!");
        return ResponseEntity.ok().body(restaurantTableService.getAllRestaurantTables());
    }
}
