package ch.etml.es.payroll.controllers;

import ch.etml.es.payroll.repositories.EmployeeRepository;
import ch.etml.es.payroll.entities.Employee;
import ch.etml.es.payroll.services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    EmployeeController(EmployeeRepository repository){
        this.repository = repository;
    }

    /* curl sample :
    curl -X GET localhost:8080/api/v1/employees | jq
    */
    @GetMapping("")
    List<Employee> all(){
        return repository.findAll();
    }

    /* curl sample :
    curl -X GET localhost:8080/api/v1/employees/1
    */
    @GetMapping("/{id}")
    Employee one(@PathVariable Long id){
        return repository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    /* curl sample :
        curl -i -X POST localhost:8080/api/v1/employees ^
            -H "Content-type:application/json" ^
            -d "{\"name\": \"Russel George\", \"role\": \"gardener\"}"
    */
    @PostMapping("")
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee) {
        Employee created = EmployeeService.create(employee);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(created);
    }

    /* curl sample :
    curl -i -X PUT localhost:8080/api/v1/employees/2 ^
        -H "Content-type:application/json" ^
        -d "{\"name\": \"Samwise Bing\", \"role\": \"peer-to-peer\"}"
    */
    @PutMapping("/{id}")
    public ResponseEntity<Employee> upsertEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee
    ) {
        Optional<Employee> existing = repository.findById(id);

        employee.setId(id);
        Employee saved = repository.save(employee);

        if (existing.isPresent()) {
            return ResponseEntity.ok(saved);
        } else {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .build()
                    .toUri();

            return ResponseEntity.created(location).body(saved);
        }
    }

    /* curl sample :
        curl -i -X DELETE localhost:8080/api/v1/employees/2
        */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {

        if (!repository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }

        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
