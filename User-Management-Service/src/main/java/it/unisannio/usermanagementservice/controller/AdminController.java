package it.unisannio.usermanagementservice.controller;

import it.unisannio.usermanagementservice.entity.Admin;
import it.unisannio.usermanagementservice.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/add")
    public ResponseEntity addAdmin(@RequestBody Admin admin) {
        return adminService.addAdmin(admin);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteAdmin(@PathVariable Integer id) {
        if(adminService.findAdminById(id) == null)
            return ResponseEntity.notFound().build();
        return adminService.deleteAdmin(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateAdmin(@PathVariable Integer id, @RequestBody Admin admin) {
        return adminService.updateAdmin(admin, id);
    }

    @PutMapping("/restaurant/{id}/{adminId}")
    public ResponseEntity updateRestaurant(@PathVariable Integer id, @PathVariable Integer adminId) {
        return adminService.updateRestaurant(id, adminId);
    }

    @GetMapping("/{id}")
    public ResponseEntity getAdminById(@PathVariable Integer id) {
        if(adminService.findAdminById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(adminService.findAdminById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllAdmins() {
        if(!(adminService.findAllAdmins().iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(adminService.findAllAdmins());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity getAdminByUsername(@PathVariable String username) {
        if(adminService.findAdminByUsername(username) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(adminService.findAdminByUsername(username));
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity getAdminRestaurantById(@PathVariable Integer id) {
        if(!(adminService.findByRestaurantId(id).iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(adminService.findByRestaurantId(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity getAdminByEmail(@PathVariable String email) {
        if(adminService.findByEmail(email) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(adminService.findByEmail(email));
    }
}
