package ch.etml.es.payroll.controllers;

import ch.etml.es.payroll.config.EmployeeServiceProperties;
import ch.etml.es.payroll.entities.Department;
import ch.etml.es.payroll.repositories.DepartmentRepository;
import ch.etml.es.payroll.services.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DepartmentHireTest {

    @Mock
    private DepartmentRepository repository;

    private Department department;

    private DepartmentService departmentService;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setAcronym("IT");

        // Create a real properties object
        EmployeeServiceProperties props = new EmployeeServiceProperties();
        props.setUrl("http://localhost:8081/api/v1/employees");

        this.departmentService = new DepartmentService(repository, props);
    }

    @Test
    void shouldHireEmployeeSuccessfully() {
        Long employeeId = 1L;

        when(repository.findById(1L)).thenReturn(Optional.of(department));
        when(repository.save(any(Department.class))).thenReturn(department);

        Department result = departmentService.hireEmployee(1L, employeeId);

        assertNotNull(result);
        assertTrue(result.getEmployeeIds().contains(employeeId));

        verify(repository).save(department);
    }

    @Test
    void shouldThrowWhenEmployeeAlreadyHired() {
        Long employeeId = 10L;
        department.getEmployeeIds().add(employeeId);

        when(repository.findById(1L)).thenReturn(Optional.of(department));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                departmentService.hireEmployee(1L, employeeId)
        );

        assertEquals("Employee 10 is already hired", ex.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowWhenDepartmentNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Long employeeId = 10L;

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                departmentService.hireEmployee(1L, employeeId)
        );

        assertEquals("Could not find department: 1", ex.getMessage());
        verify(repository, never()).save(any());
    }
}