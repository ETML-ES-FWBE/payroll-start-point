package ch.etml.es.payroll.controllers;

public class EmployeeAlreadyExistsException extends RuntimeException{

    public EmployeeAlreadyExistsException(String name){
        super("Employee " + name + " already exists");}
}
