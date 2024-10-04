package it.unisannio.usermanagementservice.service;

import it.unisannio.usermanagementservice.entity.Admin;
import it.unisannio.usermanagementservice.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    public ResponseEntity addAdmin(Admin admin) {
        if (admin.getUsername().isEmpty() || admin.getPassword().isEmpty() || admin.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parametri mancanti!");
        }

        if(adminRepository.findByUsername(admin.getUsername()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lo username è già in uso!");
        }

        if(adminRepository.findByEmail(admin.getEmail()) != null){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("L'email è già in uso!");
        }

        if(admin.getPassword().length() < 5)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La password è troppo corta!");

        admin.setPassword(new BCryptPasswordEncoder(12).encode(admin.getPassword()));
        adminRepository.save(admin);
        return ResponseEntity.status(HttpStatus.CREATED).body("Admin aggiunto con successo!");
    }

    public ResponseEntity deleteAdmin(int id) {
        adminRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Admin eliminato con successo!");
    }

    public ResponseEntity updateAdmin(Admin admin, int id) {
        Admin oldAdmin = adminRepository.findById(id).orElse(null);
        if(oldAdmin == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin non trovato!");

        oldAdmin.setUsername(admin.getUsername());
        oldAdmin.setPassword(new BCryptPasswordEncoder(12).encode(admin.getPassword()));
        oldAdmin.setEmail(admin.getEmail());
        oldAdmin.setRestaurantId(admin.getRestaurantId());
        adminRepository.save(oldAdmin);

        return ResponseEntity.status(HttpStatus.OK).body("Admin aggiornato con successo!");
    }

    public Admin findAdminById(int id) {
        return adminRepository.findById(id).orElse(null);
    }

    public Iterable<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin findAdminByUsername(String username) {
        return adminRepository.findByUsername(username);
    }

    public Iterable<Admin> findByRestaurantId(int restaurantId) {
        return adminRepository.findByRestaurantId(restaurantId);
    }

    public Admin findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

    public ResponseEntity updateRestaurant(int id, int adminId){
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if(admin == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin non trovato!");
        admin.setRestaurantId(id);
        adminRepository.save(admin);
        return ResponseEntity.status(HttpStatus.OK).body("Admin aggiornato con successo!");
    }
}
