package com.rajat.EmployeeManagementPortal.Service;

import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.STATUS;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.request.ChangePasswordRequest;
import com.rajat.EmployeeManagementPortal.request.EmployeeRequest;
import com.rajat.EmployeeManagementPortal.response.AdminUserListResponse;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.ManagerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ManagerServiceTest {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private ManagerRepository managerRepository;

  @Mock
  private RequestRepository requestRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  AutoCloseable closeable;

  @InjectMocks
  private ManagerService managerService;

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    closeable.close();
  }

  @Test
  void testViewAll() {
    LocalDate date = LocalDate.parse("1994-12-12");
    User user = User.builder()
      .userId(Long.parseLong("1"))
      .email("abc@gmail.com")
      .password("1234")
      .name("John")
      .contact(9876543210L)
      .gender('M')
      .dateOfBirth(date)
      .role(USER_ROLE.EMPLOYEE)
      .build();

    List<User> users = new ArrayList<>();
    users.add(user);

    when(userRepository.findAll()).thenReturn(users);

    List<UserListResponse> userList = managerService.viewAll();

    assertEquals(1, userList.size());
    assertEquals("abc@gmail.com", userList.get(0).getEmail());
  }

  @Test
  void testRequestEmployee_success() {
    EmployeeRequest request = new EmployeeRequest(1L, "Skill", 2, STATUS.PENDING);
    User mockUser = new User();
    mockUser.setUserId(1L);
    mockUser.setName("John");
    mockUser.setEmail("abc@example.com");
    mockUser.setPassword("password");
    mockUser.setRole(USER_ROLE.MANAGER);

    when(userRepository.findByUserId(1L)).thenReturn(Optional.of(mockUser));

    String result = managerService.requestEmployee(request);

    assertEquals("Request submitted successfully.", result);
    verify(requestRepository, times(1)).save(any(Request.class));
  }

  @Test
  void testRequestEmployee_UserNotFound() {
    EmployeeRequest request = new EmployeeRequest(1L, "Skill", 2, STATUS.PENDING);
    when(userRepository.findByUserId(1L)).thenReturn(Optional.empty());

    Exception exception = assertThrows(RuntimeException.class, () -> {
      managerService.requestEmployee(request);
    });

    assertEquals("No user found with provided id", exception.getMessage());
  }

  @Test
  void testGetAllEmployees() {
    String skillName = "Java";
    boolean unassigned = false;

    List<EmployeeListResponse> employees = Collections.singletonList(new EmployeeListResponse());

    when(employeeRepository.findAllEmployeeDetails(skillName, unassigned)).thenReturn(employees);

    List<EmployeeListResponse> result = managerService.getAllEmployees(skillName, unassigned);
    assertEquals(1, result.size());
  }

  @Test
  void changePassword() throws Exception {
    ChangePasswordRequest request = new ChangePasswordRequest("oldPassword", "newPassword123");
    User mockUser = new User();
    mockUser.setPassword("encodedPassword");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    when(passwordEncoder.matches("oldPassword", "encodedPassword")).thenReturn(true);
    when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");

    String result = managerService.changePassword(request);

    assertEquals("Password changed successfully", result);
    verify(userRepository, times(1)).save(mockUser);
  }

  @Test
  void testChangePassword_InvalidOldPassword() {
    ChangePasswordRequest request = new ChangePasswordRequest("wrongPassword", "newPassword123");
    User mockUser = new User();
    mockUser.setPassword("encodedPassword");

    Authentication authentication = mock(Authentication.class);
    when(authentication.getPrincipal()).thenReturn(mockUser);
    SecurityContextHolder.getContext().setAuthentication(authentication);
    when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

    Exception exception = assertThrows(BadCredentialsException.class, () -> {
      managerService.changePassword(request);
    });

    assertEquals("Password doesn't match with existing password!", exception.getMessage());
  }
}