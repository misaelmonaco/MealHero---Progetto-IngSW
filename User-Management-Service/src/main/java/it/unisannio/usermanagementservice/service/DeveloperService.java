package it.unisannio.usermanagementservice.service;

import it.unisannio.usermanagementservice.entity.Developer;
import it.unisannio.usermanagementservice.repository.DeveloperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DeveloperService {

    @Autowired
    private DeveloperRepository developerRepository;

    public ResponseEntity addDeveloper(Developer developer) {
        if(developer.getUsername().isEmpty() || developer.getPassword().isEmpty() || developer.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parametri mancanti!");
        }

        if(developerRepository.findByUsername(developer.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lo username è già in uso!");
        }

        if(developerRepository.findByEmail(developer.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("L'email è già in uso!");
        }

        if(developer.getPassword().length() < 5)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password troppo corta!");

        developer.setPassword(new BCryptPasswordEncoder(12).encode(developer.getPassword()));
        developerRepository.save(developer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Developer aggiunto con successo!");
    }

    public ResponseEntity deleteDeveloper(Integer id) {
        developerRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Developer rimosso con successo!");
    }

    public ResponseEntity updateDeveloper(Developer developer, int id) {
        Developer oldDeveloper = developerRepository.findById(id).orElse(null);
        if(oldDeveloper == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Developer non trovato!");
        }

        oldDeveloper.setUsername(developer.getUsername());
        oldDeveloper.setPassword(new BCryptPasswordEncoder(12).encode(developer.getPassword()));
        oldDeveloper.setEmail(developer.getEmail());

        developerRepository.save(oldDeveloper);
        return ResponseEntity.status(HttpStatus.OK).body("Developer aggiornato con successo!");
    }

    public Developer getDeveloperById(Integer id) {
        return developerRepository.findById(id).orElse(null);
    }

    public Developer getDeveloperByUsername(String username) {
        return developerRepository.findByUsername(username);
    }

    public Iterable<Developer> findAllDevelopers() {
        return developerRepository.findAll();
    }

    public Developer findByEmail(String email) {
        return developerRepository.findByEmail(email);
    }
}
