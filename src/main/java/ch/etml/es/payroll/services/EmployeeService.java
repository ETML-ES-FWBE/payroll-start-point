package ch.etml.es.payroll.services;

import ch.etml.es.payroll.Controllers.EmployeeAlreadyExistException;
import ch.etml.es.payroll.Entities.Employee;
import ch.etml.es.payroll.Repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {
    private static EmployeeRepository repository =null;

    public EmployeeService(EmployeeRepository repository) {
        EmployeeService.repository = repository;
    }

    public static Employee hire(Employee employee){
       Employee existing = repository.findByName(employee.getName())
               .orElse(null);

        if(existing!=null){
          throw new EmployeeAlreadyExistException(employee.getName());
        }
        return repository.save(employee);
    }
}
