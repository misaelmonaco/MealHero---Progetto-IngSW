package it.unisannio.menuservice.service;

import it.unisannio.menuservice.entity.MenuItem;
import it.unisannio.menuservice.repository.MenuItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MenuItemService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    public ResponseEntity addMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body("Nuovo item aggiunto con successo");
    }

    public ResponseEntity deleteMenuItem(int menuItem) {
        menuItemRepository.deleteById(menuItem);
        return ResponseEntity.status(HttpStatus.OK).body("Item eliminato");
    }

    public ResponseEntity updateMenuItem(MenuItem menuItem, int menuItemId) {
        MenuItem existingItem = menuItemRepository.findById(menuItemId).orElse(null);
        if (existingItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item non trovato");
        }

        existingItem.setName(menuItem.getName());
        existingItem.setDescription(menuItem.getDescription());
        existingItem.setPrice(menuItem.getPrice());
        existingItem.setMenuId(menuItem.getMenuId());
        menuItemRepository.save(existingItem);

        return ResponseEntity.status(HttpStatus.OK).body("Item aggiornato con successo");
    }

    public Iterable<MenuItem> getMenuItems() {
        return menuItemRepository.findAll();
    }

    public MenuItem getMenuItemById(int id) {
        return menuItemRepository.findById(id).orElse(null);
    }

    public Iterable<MenuItem> getMenuItemByMenuId(Integer id) {
        return menuItemRepository.findByMenuIdId(id);
    }
}
