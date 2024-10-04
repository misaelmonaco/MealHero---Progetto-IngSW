package it.unisannio.usermanagementservice.service;

import it.unisannio.usermanagementservice.entity.Employee;
import it.unisannio.usermanagementservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public ResponseEntity addEmployee(Employee employee) {
        if(employee.getUsername().isEmpty() || employee.getPassword().isEmpty() || employee.getEmail().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parametri mancanti!");
        }

        if(employeeRepository.findByUsername(employee.getUsername()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Lo username è già in uso!");
        }

        if(employeeRepository.findByEmail(employee.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("L'email è già in uso!");
        }
        if(employee.getPassword().length() < 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La password è troppo corta!");
        }

        employee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
        employeeRepository.save(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body("Employee aggiunto con successo!");
    }

    public ResponseEntity deleteEmployee(int id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Employee eliminato con successo!");
    }

    public ResponseEntity updateEmployee(Employee employee, int id) {
        Employee oldEmployee = employeeRepository.findById(id).orElse(null);
        if(oldEmployee == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee non trovato!");

        oldEmployee.setUsername(employee.getUsername());
        oldEmployee.setPassword(new BCryptPasswordEncoder().encode(employee.getPassword()));
        oldEmployee.setEmail(employee.getEmail());
        oldEmployee.setRestaurantId(employee.getRestaurantId());
        employeeRepository.save(oldEmployee);
        return ResponseEntity.status(HttpStatus.OK).body("Employee aggiornato con successo!");
    }

    public Employee findEmployeeById(int id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee findEmployeeByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    public Iterable<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public Iterable<Employee> findEmployeeByRestaurantId(int id) {
        return employeeRepository.findByRestaurantId(id);
    }

    public Employee findByEmail(String email) { return employeeRepository.findByEmail(email); }
}
