package ch.etml.es.payroll.services;

import ch.etml.es.payroll.controllers.EmployeeAlreadyExistsException;
import ch.etml.es.payroll.entities.Employee;
import ch.etml.es.payroll.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private static EmployeeRepository repository = null;

    public EmployeeService(EmployeeRepository repository) {
        EmployeeService.repository = repository;
    }

    public static Employee hire(Employee employee) {
        Employee existing = repository.findByName(employee.getName())
                .orElse(null);

        if (existing != null) {
            throw new EmployeeAlreadyExistsException(employee.getName());
        }
        return repository.save(employee);
    }
}
