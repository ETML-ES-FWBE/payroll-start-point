package ch.etml.es.payroll.Repositories;
import ch.etml.es.payroll.Entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<ch.etml.es.payroll.Entities.Employee, Long>{


    Optional<Employee> findByName(String name);
}
