package ch.etml.es.payroll.controllers;

public class EmployeeAlreadyHiredException extends RuntimeException{

    public EmployeeAlreadyHiredException(Long id){
        super("Employee " + id + " is already hired");
    }
}
