package com.rajat.EmployeeManagementPortal.Repository;

import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ManagerRepositoryTest {

  @Autowired
  private ManagerRepository managerRepository;

  @Autowired
  private UserRepository userRepository;

  @Test
  void testSaveManager() {
    User user = new User();
    user.setEmail("abc@example.com");
    user.setPassword("password");
    user.setRole(USER_ROLE.MANAGER);
    User savedUser = userRepository.save(user);

    Manager manager = new Manager();
    manager.setUserId(savedUser.getUserId());
    manager.setUser(savedUser);
    savedUser.setManager(manager);
    Manager savedManager = managerRepository.save(manager);

    assertThat(savedManager).isNotNull();
    assertEquals("abc@example.com", savedManager.getUser().getEmail());
  }

  @Test
  void testDeleteManager() {
    User user = new User();
    user.setEmail("abc@example.com");
    user.setPassword("password");
    user.setRole(USER_ROLE.MANAGER);
    User savedUser = userRepository.save(user);

    Manager manager = new Manager();
    manager.setUserId(savedUser.getUserId());
    manager.setUser(savedUser);
    savedUser.setManager(manager);

    managerRepository.save(manager);
    managerRepository.deleteById(manager.getUserId());

    Optional<Manager> deletedManager = managerRepository.findById(1L);

    assertThat(deletedManager).isEmpty();
  }

  @Test
  void testManagerNotFound() {
    User user = new User();
    user.setEmail("abc@example.com");
    user.setPassword("password");
    user.setRole(USER_ROLE.MANAGER);
    User savedUser = userRepository.save(user);

    Manager manager = new Manager();
    manager.setUserId(savedUser.getUserId());
    manager.setUser(savedUser);
    savedUser.setManager(manager);
    managerRepository.save(manager);

    Optional<Manager> foundManager = managerRepository.findById(999L);
    assertFalse(foundManager.isPresent());
  }

}
