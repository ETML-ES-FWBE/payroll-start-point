package ch.etml.es.payroll.controllers;

import ch.etml.es.payroll.entities.Department;
import ch.etml.es.payroll.services.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /* GET all departments */
    @GetMapping("")
    public List<Department> all() {
        return departmentService.findAll();
    }

    /* GET one department by ID */
    @GetMapping("/{id}")
    public Department one(@PathVariable Long id) {
        return departmentService.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));
    }

    /* CREATE a new department */
    @PostMapping("")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        Department created = departmentService.create(department);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(created);
    }

    /* HIRE an employee (add employee ID to department) */
    @PostMapping("/{departmentId}/employees")
    public ResponseEntity<Department> hireEmployee(
            @PathVariable Long departmentId,
            @RequestBody Map<String, Long> body
    ) {
        Long employeeId = body.get("employee_id");
        Department updated = departmentService.hireEmployee(departmentId, employeeId);

        return ResponseEntity.ok(updated);
    }
}