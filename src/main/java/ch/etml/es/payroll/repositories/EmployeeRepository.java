package ch.etml.es.payroll.repositories;

import ch.etml.es.payroll.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<ch.etml.es.payroll.entities.Employee, Long>{
    Optional<Employee> findByName(String name);
}
