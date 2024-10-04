package it.unisannio.menuservice.controller;

import it.unisannio.menuservice.entity.MenuItem;
import it.unisannio.menuservice.service.MenuItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menuItem")
public class MenuItemController {

    @Autowired
    private MenuItemService menuItemService;

    @PostMapping("/add")
    public ResponseEntity addMenuItem(@RequestBody MenuItem menuItem) {
        return menuItemService.addMenuItem(menuItem);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity removeMenuItem(@PathVariable int id) {
        if(menuItemService.getMenuItemById(id) == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return menuItemService.deleteMenuItem(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateMenuItem(@RequestBody MenuItem menuItem, @PathVariable int id) {
        return menuItemService.updateMenuItem(menuItem, id);
    }

    @GetMapping("/all")
    public ResponseEntity getAllMenuItems() {
        if(!(menuItemService.getMenuItems().iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu items non trovati");
        return ResponseEntity.ok(menuItemService.getMenuItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity getMenuItemById(@PathVariable int id) {
        if(menuItemService.getMenuItemById(id) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu item non trovato");
        return ResponseEntity.ok(menuItemService.getMenuItemById(id));
    }

    @GetMapping("/menuId/{menuId}")
    public ResponseEntity getMenuItemByMenuId(@PathVariable int menuId) {
        if(!(menuItemService.getMenuItemByMenuId(menuId).iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu items non trovati");
        return ResponseEntity.ok(menuItemService.getMenuItemByMenuId(menuId));
    }
}
