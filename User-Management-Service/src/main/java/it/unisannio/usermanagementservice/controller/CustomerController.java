package it.unisannio.usermanagementservice.controller;

import it.unisannio.usermanagementservice.entity.Customer;
import it.unisannio.usermanagementservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/add")
    public ResponseEntity addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteCustomer(@PathVariable int id) {
        if(customerService.findCustomerById(id) == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return customerService.deleteCustomer(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateCustomer(@PathVariable int id, @RequestBody Customer customer) {
        return customerService.updateCustomer(customer, id);
    }

    @PutMapping("/score/{score}/{id}")
    public ResponseEntity updateScore(@PathVariable int score, @PathVariable int id) {
        return customerService.updateScore(score, id);
    }

    @PutMapping("/score/subtract/{score}/{id}")
    public ResponseEntity subtractScore(@PathVariable int score, @PathVariable int id) {
        return customerService.subtractScore(score, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCustomer(@PathVariable int id) {
        if(customerService.findCustomerById(id) == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findCustomerById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllCustomers() {
        if(!(customerService.findAllCustomers().iterator().hasNext()))
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findAllCustomers());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity getCustomerByUsername(@PathVariable String username) {
        if(customerService.findCustomerByUsername(username) == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findCustomerByUsername(username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity getCustomerByEmail(@PathVariable String email) {
        if(customerService.findByEmail(email) == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        return ResponseEntity.status(HttpStatus.OK).body(customerService.findByEmail(email));
    }
}
