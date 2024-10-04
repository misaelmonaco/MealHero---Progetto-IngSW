package it.unisannio.usermanagementservice.repository;

import it.unisannio.usermanagementservice.entity.Employee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Integer> {

    @Query
    Employee findByUsername(String username);

    @Query
    Employee findByEmail(String email);

    @Query("SELECT e FROM Employee e WHERE e.restaurantId = :id")
    Iterable<Employee> findByRestaurantId(int id);
}
