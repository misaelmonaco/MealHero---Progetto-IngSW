package it.unisannio.usermanagementservice.controller;

import it.unisannio.usermanagementservice.entity.Developer;
import it.unisannio.usermanagementservice.repository.DeveloperRepository;
import it.unisannio.usermanagementservice.service.DeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/developer")
public class DeveloperController {

    @Autowired
    private DeveloperService developerService;

    @PostMapping("/add")
    public ResponseEntity addDeveloper(@RequestBody Developer developer) {
        return developerService.addDeveloper(developer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteDeveloper(@PathVariable Integer id) {
        if(developerService.getDeveloperById(id) == null)
            return ResponseEntity.notFound().build();
        return developerService.deleteDeveloper(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateDeveloper(@PathVariable Integer id, @RequestBody Developer developer) {
        return developerService.updateDeveloper(developer, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDeveloperById(@PathVariable Integer id) {
       if(developerService.getDeveloperById(id) == null)
           return ResponseEntity.notFound().build();
       return ResponseEntity.ok().body(developerService.getDeveloperById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllDevelopers() {
        if(!(developerService.findAllDevelopers().iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(developerService.findAllDevelopers());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity getAllDevelopersByNickname(@PathVariable String username) {
        if(developerService.getDeveloperByUsername(username) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(developerService.getDeveloperByUsername(username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity getAllDevelopersByEmail(@PathVariable String email) {
        if(developerService.findByEmail(email) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().body(developerService.findByEmail(email));
    }
}
