package it.unisannio.menuservice.controller;

import it.unisannio.menuservice.entity.Menu;
import it.unisannio.menuservice.entity.MenuItem;
import it.unisannio.menuservice.repository.MenuItemRepository;
import it.unisannio.menuservice.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @PostMapping("/add")
    public ResponseEntity addMenu(@RequestBody Menu menu) {
        return menuService.addMenu(menu);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity removeMenu(@PathVariable int id) {
        if(menuService.findMenuById(id) == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return menuService.deleteMenu(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMenu(@RequestBody Menu menu, @PathVariable int id) {
        return menuService.updateMenu(menu, id);
    }

    @PutMapping("/addItem/{id}")
    public ResponseEntity addItem(@RequestBody MenuItem item, @PathVariable int id) {
        if(menuService.findMenuById(id) == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return menuService.addMenuItem(item, id);
    }

    @PutMapping("/addItems/{id}")
    public ResponseEntity addItems(@PathVariable int id, @RequestBody List<MenuItem> items) {
        return menuService.addMenuItems(items, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getMenu(@PathVariable int id) {
       if(menuService.findMenuById(id) == null)
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu non trovato");
       return ResponseEntity.ok(menuService.findMenuById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllMenu() {
        if(!(menuService.findAll().iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu non trovati");
        return ResponseEntity.status(HttpStatus.OK).body(menuService.findAll());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity getRestaurantMenus(@PathVariable int restaurantId) {
        if(!(menuService.findByRestaurantId(restaurantId).iterator().hasNext())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu non trovati");
        }
        return ResponseEntity.status(HttpStatus.OK).body(menuService.findByRestaurantId(restaurantId));
    }
}
