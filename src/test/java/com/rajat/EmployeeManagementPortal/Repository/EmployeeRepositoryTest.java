package com.rajat.EmployeeManagementPortal.Repository;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EmployeeRepositoryTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Test
  void findUnassignedEmployeesTest() {
    com.rajat.EmployeeManagementPortal.model.User user1 = com.rajat.EmployeeManagementPortal.model.User.builder()
      .userId(101L)
      .build();

    com.rajat.EmployeeManagementPortal.model.User user2 = com.rajat.EmployeeManagementPortal.model.User.builder()
      .userId(102L)
      .build();

    Employee emp1 = Employee.builder()
      .userId(101L)
      .user(user1)
      .build();

    Employee emp2 = Employee.builder()
      .userId(102L)
      .user(user2)
      .project(Project.builder().projectName("Quiz Application").build())
      .build();

    employeeRepository.save(emp1);
    employeeRepository.save(emp2);
    List<Employee> unassignedEmployees = employeeRepository.findUnassignedEmployees().get();

    assertThat(unassignedEmployees).isNotNull();
    assertThat(unassignedEmployees.size()).isEqualTo(1);
  }
}