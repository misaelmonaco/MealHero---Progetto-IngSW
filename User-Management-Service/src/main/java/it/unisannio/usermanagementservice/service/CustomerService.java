package it.unisannio.usermanagementservice.service;

import it.unisannio.usermanagementservice.entity.Customer;
import it.unisannio.usermanagementservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public ResponseEntity addCustomer(Customer customer) {
        if(customer.getUsername().isEmpty() || customer.getPassword().isEmpty() || customer.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parametri mancanti!");
        }

        if(customerRepository.findByUsername(customer.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lo username è già in uso!");
        }

        if(customerRepository.findByEmail(customer.getEmail()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("L'email è già in uso!");

        if(customer.getPassword().length() < 5)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password troppo corta!");

        customer.setPassword(new BCryptPasswordEncoder(12).encode(customer.getPassword()));
        customerRepository.save(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer aggiunto con successo!");
    }

    public ResponseEntity deleteCustomer(Integer id) {
        customerRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Customer eliminato con successo!");
    }

    public ResponseEntity updateCustomer(Customer customer, int id) {
        Customer oldCustomer = customerRepository.findById(id).orElse(null);

        if(oldCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer non trovato!");
        }

        oldCustomer.setUsername(customer.getUsername());
        oldCustomer.setPassword(new BCryptPasswordEncoder(12).encode(customer.getPassword()));
        oldCustomer.setEmail(customer.getEmail());
        oldCustomer.setScore(customer.getScore());
        customerRepository.save(oldCustomer);

        return ResponseEntity.status(HttpStatus.OK).body("Customer aggiornato con successo!");
    }

    public ResponseEntity updateScore(int score, int id) {
        Customer oldCustomer = customerRepository.findById(id).orElse(null);
        if(oldCustomer == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer non trovato!");
        }

        int newScore = oldCustomer.getScore() + score;
        oldCustomer.setScore(newScore);
        customerRepository.save(oldCustomer);
        return ResponseEntity.status(HttpStatus.OK).body("Customer aggiornato con successo!");
    }

    public Customer findCustomerById(int id) {
       return customerRepository.findById(id).orElse(null);
    }

    public Iterable<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer findCustomerByUsername(String username) {
        return customerRepository.findByUsername(username);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
