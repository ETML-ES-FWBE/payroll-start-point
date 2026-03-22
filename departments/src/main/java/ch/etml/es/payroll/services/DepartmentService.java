package ch.etml.es.payroll.services;

import ch.etml.es.payroll.config.EmployeeServiceProperties;
import ch.etml.es.payroll.controllers.DepartmentAlreadyExistsException;
import ch.etml.es.payroll.controllers.DepartmentNotFoundException;
import ch.etml.es.payroll.controllers.EmployeeAlreadyHiredException;
import ch.etml.es.payroll.controllers.EmployeeNotFoundException;
import ch.etml.es.payroll.entities.Department;
import ch.etml.es.payroll.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Service
public class DepartmentService {

    private static DepartmentRepository repository = null;
    static final RestTemplate restTemplate = new RestTemplate();
    private final String employeeServiceUrl;

    public DepartmentService(DepartmentRepository repository, EmployeeServiceProperties props) {
        DepartmentService.repository = repository;
        this.employeeServiceUrl = props.getUrl();
    }

    public static Department create(Department department) {
        Department existing = repository.findByAcronym(department.getAcronym())
                .orElse(null);

        if (existing != null) {
            throw new DepartmentAlreadyExistsException(department.getAcronym());
        }
        return repository.save(department);
    }

    public Department hireEmployee(Long departmentId, Long employeeId) {

        Department department = repository.findById(departmentId)
                .orElseThrow(() -> new DepartmentNotFoundException(departmentId));

        // Check already hired
        if (department.hasEmployee(employeeId)) {
            throw new EmployeeAlreadyHiredException(employeeId);
        }

        // Check employee exists (remote call)
        try {
            restTemplate.getForEntity(this.employeeServiceUrl+ "/" + employeeId, Void.class);
            // employee exists
        } catch (HttpClientErrorException.NotFound e) {
            throw new EmployeeNotFoundException(employeeId);
        }

        // Add employee
        department.addEmployee(employeeId);

        return repository.save(department);
    }

    public List<Department> findAll() {
        return repository.findAll();
    }

    public Optional<Department> findById(Long id) {
        return repository.findById(id);
    }
}
