package it.unisannio.menuservice.service;

import it.unisannio.menuservice.entity.Menu;
import it.unisannio.menuservice.entity.MenuItem;
import it.unisannio.menuservice.repository.MenuItemRepository;
import it.unisannio.menuservice.repository.MenuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;

    public ResponseEntity addMenu(Menu menu) {
        menuRepository.save(menu);
        return ResponseEntity.status(HttpStatus.CREATED).body("Menù aggiunto con successo!");
    }

    @Transactional
    public ResponseEntity deleteMenu(int menuId) {
        menuItemRepository.deleteByMenuId(menuId);
        menuRepository.deleteById(menuId);
        return ResponseEntity.status(HttpStatus.OK).body("Menù rimosso con succsso!");
    }

    public ResponseEntity updateMenu(Menu menu, int menuId) {
        Menu existingMenu = menuRepository.findById(menuId).orElse(null);
        if (existingMenu == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menù non trovato!");
        }

        existingMenu.setName(menu.getName());
        existingMenu.setDescription(menu.getDescription());
        existingMenu.setRestaurantId(menu.getRestaurantId());
        menuRepository.save(existingMenu);

        return ResponseEntity.status(HttpStatus.OK).body("Menu aggiornato con successo!");
    }

    public ResponseEntity addMenuItems(List<MenuItem> menuItems, int menuId) {
        Menu existingMenu = menuRepository.findById(menuId).orElse(null);
        if(existingMenu == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu non trovato!");
        }
        menuItems.forEach(menuItem -> menuItem.setMenuId(menuId));
        menuItems.forEach(menuItem -> menuItemRepository.save(menuItem));
        return ResponseEntity.status(HttpStatus.OK).body("Menu aggiornato con successo!");
    }

    public ResponseEntity addMenuItem(MenuItem menuItem, int menuId) {
        Menu existingMenu = menuRepository.findById(menuId).orElse(null);
        if(existingMenu == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Menu non trovato!");
        }
        menuItem.setMenuId(menuId);
        menuItemRepository.save(menuItem);
        return ResponseEntity.status(HttpStatus.OK).body("Menu aggiornato con successo!");
    }

    public Menu findMenuById(int menuId) {
       return menuRepository.findById(menuId).orElse(null);
    }

    public Iterable<Menu> findByRestaurantId(int restaurantId) {
       return menuRepository.findByRestaurantId(restaurantId).orElse(null);
    }

    public Iterable<Menu> findAll() {
        return menuRepository.findAll();
    }
}
