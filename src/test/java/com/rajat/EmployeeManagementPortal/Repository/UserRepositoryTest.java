package com.rajat.EmployeeManagementPortal.Repository;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUserTest() {
        User user = User.builder()
                .email("abc@gmail.com")
                .password("1234")
                .name("abc")
                .contact(9039567890L)
                .gender('F')
                .role(USER_ROLE.EMPLOYEE)
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void getUserByEmailTest() {
        User user = User.builder()
                .email("abc@gmail.com")
                .password("1234")
                .name("abc")
                .contact(9039567890L)
                .role(USER_ROLE.EMPLOYEE)
                .build();

        userRepository.save(user);
        User foundUser = userRepository.findByEmail(user.getEmail()).get();

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void findAllUsersTest() {
        User user = User.builder()
                .email("abc@gmail.com")
                .password("1234")
                .name("abc")
                .contact(9039567890L)
                .role(USER_ROLE.EMPLOYEE)
                .build();

        User user2 = User.builder()
                .email("john@gmail.com")
                .password("qwerty")
                .name("john")
                .contact(9039512390L)
                .role(USER_ROLE.MANAGER)
                .build();

        userRepository.save(user);
        userRepository.save(user2);
        List<User> userList = userRepository.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isEqualTo(2);

    }

    @Test
    public void updateUserTest() {
        User user = User.builder()
                .email("abc@gmail.com")
                .password("1234")
                .name("abc")
                .gender('F')
                .contact(9039567890L)
                .role(USER_ROLE.EMPLOYEE)
                .build();

        userRepository.save(user);

        User savedUser = userRepository.findByEmail(user.getEmail()).get();
        savedUser.setName("Adam");
        savedUser.setContact(9179098765L);

        User updatedUser = userRepository.save(savedUser);

        Assertions.assertThat(updatedUser.getName()).isNotNull();
        Assertions.assertThat(updatedUser.getContact()).isNotNull();
    }

    @Test
    public void deleteUserTest() {
        User user = User.builder()
                .email("abc@gmail.com")
                .password("1234")
                .name("abc")
                .contact(9039567890L)
                .role(USER_ROLE.EMPLOYEE)
                .build();

        userRepository.save(user);
        userRepository.deleteByEmail(user.getEmail());

        Optional<User> returnedUser = userRepository.findByEmail(user.getEmail());

        Assertions.assertThat(returnedUser).isEmpty();
    }
}