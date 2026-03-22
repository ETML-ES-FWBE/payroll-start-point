package ch.etml.es.payroll.controllers;

public class DepartmentNotFoundException extends RuntimeException{

    public DepartmentNotFoundException(Long id){
        super("Could not find department: " + id);
    }
}
