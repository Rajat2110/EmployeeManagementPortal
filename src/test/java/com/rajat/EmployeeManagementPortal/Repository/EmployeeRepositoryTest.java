package com.rajat.EmployeeManagementPortal.Repository;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.EmployeeSkill;
import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.EmployeeSkillRepository;
import com.rajat.EmployeeManagementPortal.repository.ProjectRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class EmployeeRepositoryTest {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void testSaveEmployee() {
    User user = new User();
    user.setEmail("abc@example.com");
    user.setPassword("password");
    user.setRole(USER_ROLE.EMPLOYEE);
    User savedUser = userRepository.save(user);

    Employee employee = new Employee();
    employee.setUserId(savedUser.getUserId());
    employee.setUser(savedUser);
    savedUser.setEmployee(employee);

    Employee savedEmployee = employeeRepository.save(employee);

    assertThat(savedEmployee).isNotNull();
    assertEquals("abc@example.com", savedEmployee.getUser().getEmail());
    assertEquals(USER_ROLE.EMPLOYEE, savedEmployee.getUser().getRole());
  }

  @Test
  void testDeleteEmployee() {
    User user = new User();
    user.setEmail("abc@example.com");
    user.setPassword("password");
    user.setRole(USER_ROLE.EMPLOYEE);
    User savedUser = userRepository.save(user);

    Employee employee = new Employee();
    employee.setUserId(savedUser.getUserId());
    employee.setUser(savedUser);
    savedUser.setEmployee(employee);

    employeeRepository.save(employee);
    employeeRepository.deleteById(employee.getUserId());

    Optional<Employee> deletedEmployee = employeeRepository.findById(1L);

    assertThat(deletedEmployee).isEmpty();
  }

  @Test
  void testEmployeeNotFound() {
    User user = new User();
    user.setEmail("abc@example.com");
    user.setPassword("password");
    user.setRole(USER_ROLE.EMPLOYEE);
    User savedUser = userRepository.save(user);

    Employee employee = new Employee();
    employee.setUserId(savedUser.getUserId());
    employee.setUser(savedUser);
    savedUser.setEmployee(employee);
    employeeRepository.save(employee);

    Optional<Employee> foundEmployee = employeeRepository.findById(999L);
    assertFalse(foundEmployee.isPresent());
  }

}