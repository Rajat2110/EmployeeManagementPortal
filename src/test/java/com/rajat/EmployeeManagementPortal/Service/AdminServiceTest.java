package com.rajat.EmployeeManagementPortal.Service;

import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.STATUS;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.response.AdminUserListResponse;
import com.rajat.EmployeeManagementPortal.response.AuthenticationResponse;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import com.rajat.EmployeeManagementPortal.service.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private ManagerRepository managerRepository;

  @Mock
  private SkillRepository skillRepository;

  @Mock
  private RequestRepository requestRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  AutoCloseable autoCloseable;
  User user;

  @InjectMocks
  private AdminService adminService;

  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
    LocalDate date = LocalDate.parse("1994-12-12");
    user = User.builder()
      .userId(Long.parseLong("1"))
      .email("abc@gmail.com")
      .password("1234")
      .name("John")
      .contact(9876543210L)
      .gender('M')
      .dateOfBirth(date)
      .role(USER_ROLE.EMPLOYEE)
      .build();
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

  @Test
  void testViewAll() {
    List<User> users = new ArrayList<>();
    users.add(user);

    when(userRepository.findAll()).thenReturn(users);

    List<AdminUserListResponse> userList = adminService.viewAll();

    assertEquals(1, userList.size());
    assertEquals("abc@gmail.com", userList.get(0).getEmail());
  }

  @Test
  void testRegisterNewUser() {
    when(userRepository.save(user)).thenReturn(user);

    String response = adminService.registerNewUser(user);

    assertEquals("Registered new user successfully", response);
  }

  @Test
  void testUpdateEmployee() {
    User inputUser = new User();
    inputUser.setUserId(1L);
    inputUser.setEmail("newemail@example.com");
    inputUser.setName("New Name");
    inputUser.setContact(1234567890L);
    inputUser.setRole(USER_ROLE.MANAGER);

    User foundUser = new User();
    foundUser.setUserId(1L);
    foundUser.setEmail("oldemail@example.com");
    foundUser.setName("Old Name");
    foundUser.setContact(9078675434L);
    foundUser.setRole(USER_ROLE.EMPLOYEE);

    when(userRepository.findByUserId(1L)).thenReturn(Optional.of(foundUser));
    when(userRepository.save(any(User.class))).thenReturn(foundUser);

    String result = adminService.updateEmployee(inputUser);

    assertEquals("Updated employee details successfully.", result);
    verify(userRepository, times(1)).findByUserId(1L);
    verify(userRepository, times(1)).save(foundUser);

    assertEquals("newemail@example.com", foundUser.getEmail());
    assertEquals("New Name", foundUser.getName());
    assertEquals(1234567890L, foundUser.getContact());
    assertEquals(USER_ROLE.MANAGER, foundUser.getRole());
  }

  @Test
  void testDeleteEmployee() {
    doNothing().when(userRepository).deleteByUserId(1L);
    String response = adminService.deleteEmployee(1L);

    assertEquals("Employee deleted successfully", response);
  }

  @Test
  void testApproveRequest() {
    Request request = new Request();
    request.setStatus(STATUS.PENDING);

    when(requestRepository.findById(1L)).thenReturn(Optional.of(request));

    String response = adminService.approveRequest(1L, STATUS.APPROVED);

    assertEquals("Request handled successfully.", response);
    assertEquals(STATUS.APPROVED, request.getStatus());
  }

  @Test
  void testViewRequests() {
    Request request = new Request();
    request.setId(1L);
    request.setSkill("Java");
    request.setEmployeesRequired(4);
    request.setRequestedBy("John");
    request.setStatus(STATUS.PENDING);

    List<Request> requests = new ArrayList<>();
    requests.add(request);

    when(requestRepository.findAll()).thenReturn(requests);
    List<Request> response = adminService.viewRequests();

    assertEquals(1, response.size());
  }

  @Test
  void testAddNewSkill() {
    Skill skill = new Skill();
    String newSkill = "SQL";

    when(skillRepository.save(any(Skill.class))).thenReturn(skill);

    String response = adminService.addNewSkill(newSkill);

    assertEquals("Added new skill to the list", response);
  }

  @Test
  void testViewAllSkills() {
    List<Skill> skills = Arrays.asList(new Skill(), new Skill());
    when(skillRepository.findAll()).thenReturn(skills);

    List<Skill> response = adminService.allSkills();

    assertEquals(skills.size(), response.size());
  }

  @Test
  void testViewAllEmployees() {
    String skillName = "Java";
    boolean unassigned = false;

    List<EmployeeListResponse> employees = Collections.singletonList(new EmployeeListResponse());

    when(employeeRepository.findAllEmployeeDetails(skillName, unassigned)).thenReturn(employees);

    List<EmployeeListResponse> result = adminService.allEmployees(skillName, unassigned);
    assertEquals(1, result.size());
  }
}