package it.unisannio.usermanagementservice.controller;

import it.unisannio.usermanagementservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity getAllUsers(){
        if(!(userService.findAll().iterator().hasNext()))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Non è stato trovato alcun user!");
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable Integer id){
        if(userService.findById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity getUserByUsername(@PathVariable String username){
        if(userService.getUserByUsername(username) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @GetMapping("/role/{role}")
    public ResponseEntity getUserByRole(@PathVariable String role){
        List<String> allowedRoles = Arrays.asList("ADMIN", "USER", "DEVELOPER", "EMPLOYEE", "CUSTOMER");
        if(!allowedRoles.contains(role))
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ruolo non identificato!");
        if(userService.findByRole(role) == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.ok().body(userService.findByRole(role));
    }
}
