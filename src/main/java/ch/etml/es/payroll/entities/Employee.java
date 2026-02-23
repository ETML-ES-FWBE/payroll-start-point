package ch.etml.es.payroll.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "employees",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "name")
        }
)
public class Employee {

    private @Id
    @GeneratedValue Long id;
    private String name;
    private String role;

    public Employee(){}

    public Employee(String name, String role){
        this.setName(name);
        this.setRole(role);
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getRole(){
        return this.role.toUpperCase();
    }

    public void setRole(String role){
        this.role = role;
    }

    @Override
    public boolean equals(Object o){
        if(this == o)
            return true;
        if(!(o instanceof Employee employee))
            return false;
        return  Objects.equals(this.id, employee.id) &&
                Objects.equals(this.name, employee.name) &&
                Objects.equals(this.role, employee.role);
    }

    @Override
    public int hashCode(){
        return Objects.hash(
                this.id,
                this.name,
                this.role);
    }

    @Override
    public String toString(){
        return "Employee{" + "id=" +
                this.getId() + ", name='" +
                this.getName() + '\'' + ", role='" +
                this.getRole() + '\'' +
                '}';
    }
}
