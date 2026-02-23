package ch.etml.es.payroll.Controllers;

import ch.etml.es.payroll.Entities.Employee;

public class EmployeeAlreadyExistException extends RuntimeException {
    public EmployeeAlreadyExistException(String nameEmployee) {
        super("Employee "+ nameEmployee + " already exists");
    }
}
