package ch.etml.es.payroll.controllers;

import ch.etml.es.payroll.entities.Employee;
import ch.etml.es.payroll.PayrollApplication;
import ch.etml.es.payroll.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = PayrollApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("test")
class EmployeePostTest {

    private static final String BASE_URL = "/v1/employees";

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void given_an_empty_employee_database() {
        // GIVEN
        employeeRepository.deleteAll();
    }

    @Test
    void when_hiring_new_employee_then_employee_is_created_and_persisted() {
        // GIVEN
        Employee newEmployee = new Employee("Doe", "Supervisor");

        HttpEntity<Employee> request = new HttpEntity<>(newEmployee);

        // WHEN
        ResponseEntity<Employee> response =
                restTemplate.postForEntity(
                        BASE_URL,
                        request,
                        Employee.class
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CREATED);

        // THEN (body)
        Employee createdEmployee = response.getBody();
        assertThat(createdEmployee).isNotNull();
        assertThat(createdEmployee.getId()).isNotNull();
        assertThat(createdEmployee.getName()).isEqualTo("Doe");
        assertThat(createdEmployee.getRole()).isEqualTo("SUPERVISOR");

        // THEN (database)
        assertThat(employeeRepository.findById(createdEmployee.getId()))
                .isPresent();
    }

    @Test
    void when_hiring_existing_employee_then_conflict_is_returned() {
        // GIVEN
        Employee existingEmployee = new Employee("Doe", "Supervisor");
        employeeRepository.save(existingEmployee);

        Employee duplicateEmployee = new Employee("Doe", "Supervisor");
        HttpEntity<Employee> request = new HttpEntity<>(duplicateEmployee);

        // WHEN
        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        BASE_URL,
                        request,
                        String.class
                );

        // THEN (HTTP)
        assertThat(response.getStatusCode())
                .isEqualTo(HttpStatus.CONFLICT);

        // THEN (error message)
        assertThat(response.getBody())
                .isNotNull()
                .contains("Employee "+ existingEmployee.getName() + " already exists");
    }
}
