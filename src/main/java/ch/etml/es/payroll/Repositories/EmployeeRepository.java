package ch.etml.es.payroll.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<ch.etml.es.payroll.Entities.Employee, Long>{

}
