package it.unisannio.usermanagementservice.service;

import it.unisannio.usermanagementservice.entity.Users;
import it.unisannio.usermanagementservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Iterable<Users> findAll() {
        return userRepository.findAll();
    }

    public Users findById(int id){ return userRepository.findById(id).orElse(null);}

    public Users getUserByUsername(String username) { return userRepository.findByUsername(username); }

    public Iterable<Users> findByRole(String role) { return userRepository.findByRole(role); }
}
