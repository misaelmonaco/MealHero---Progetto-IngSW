package it.unisannio.usermanagementservice.controller;

import it.unisannio.usermanagementservice.entity.Employee;
import it.unisannio.usermanagementservice.repository.EmployeeRepository;
import it.unisannio.usermanagementservice.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/add")
    public ResponseEntity addEmployee(@RequestBody Employee employee) {
        return employeeService.addEmployee(employee);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteEmployee(@PathVariable Integer id) {
        if(employeeService.findEmployeeById(id) == null)
            return ResponseEntity.notFound().build();
        return employeeService.deleteEmployee(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updateEmployee(@PathVariable Integer id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(employee, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEmployee(@PathVariable Integer id) {
        if(employeeService.findEmployeeById(id) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeService.findEmployeeById(id));
    }

    @GetMapping("/all")
    public ResponseEntity getAllEmployees() {
        if(!(employeeService.findAllEmployees().iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeService.findAllEmployees());
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity getRestaurant(@PathVariable Integer id) {
        if(!(employeeService.findEmployeeByRestaurantId(id).iterator().hasNext()))
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeService.findEmployeeByRestaurantId(id));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity getEmployeeByUsername(@PathVariable String username) {
        if(employeeService.findEmployeeByUsername(username) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeService.findEmployeeByUsername(username));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity getEmployeeByEmail(@PathVariable String email) {
        if(employeeService.findByEmail(email) == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(employeeService.findByEmail(email));
    }
}
