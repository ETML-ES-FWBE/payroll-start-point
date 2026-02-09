package ch.etml.es.payroll.Controllers;
import ch.etml.es.payroll.Entities.Employee;
import ch.etml.es.payroll.Repositories.EmployeeRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
public class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository){
        this.repository = repository;
    }

    /* curl sample :
    curl -X GET localhost:8080/api/v1/employees | jq
    */
    @GetMapping("/api/v1/employees")
    List<ch.etml.es.payroll.Entities.Employee> all(){
        return repository.findAll();
    }

    /* curl sample :
    curl -X GET localhost:8080/api/v1/employees/1
    */
    @GetMapping("/api/v1/employees/{id}")
    ch.etml.es.payroll.Entities.Employee one(@PathVariable Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

    }
    /*
    @PostMapping("/api/v1/employees")
    @ResponseStatus(HttpStatus.CREATED)
        ch.etml.es.payroll.Entities.Employee NewEmployee(@RequestBody ch.etml.es.payroll.Entities.Employee newEmployee) {
        return repository.save(newEmployee);
    }*/
    @PostMapping("/api/v1/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public ch.etml.es.payroll.Entities.Employee newEmployee(@RequestBody ch.etml.es.payroll.Entities.Employee newEmployee) {
        boolean exists = repository.findAll().stream()
                .anyMatch(emp -> emp.getName().equalsIgnoreCase(newEmployee.getName()));

        if (exists) {
            throw new EmployeeAlreadyExistException(newEmployee);
        }

        // 3. Sinon, on enregistre
        return repository.save(newEmployee);
    }
}
