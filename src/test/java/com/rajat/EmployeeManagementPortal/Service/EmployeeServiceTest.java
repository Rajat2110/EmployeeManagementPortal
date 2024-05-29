package com.rajat.EmployeeManagementPortal.Service;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.EmployeeSkill;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.EmployeeSkillRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.request.ChangePasswordRequest;
import com.rajat.EmployeeManagementPortal.response.ProfileResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.EmployeeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private SkillRepository skillRepository;

  @Mock
  private EmployeeSkillRepository employeeSkillRepository;

  @Mock
  private SecurityContext securityContext;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private Authentication authentication;

  AutoCloseable closeable;
  User user;
  Employee employee;
  Skill skill;
  Project project;

  @InjectMocks
  private EmployeeService employeeService;

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
    SecurityContextHolder.setContext(securityContext);
    user = User.builder()
      .userId(Long.parseLong("1"))
      .email("abc@gmail.com")
      .password("1234")
      .name("John")
      .contact(9876543210L)
      .gender('M')
      .dateOfBirth("12-12-1994")
      .role(USER_ROLE.EMPLOYEE)
      .build();

    project = Project.builder()
      .id(1L)
      .projectName("Quiz Application")
      .build();

    employee = Employee.builder()
      .userId(Long.parseLong("1"))
      .user(user)
      .project(project)
      .build();

    skill = Skill.builder()
      .skillName("Java")
      .build();
  }

  @AfterEach
  void tearDown() throws Exception {
    closeable.close();
  }

  @Test
  void testViewAllEmployees() {
    //Arrange
    List<User> users = new ArrayList<>();
    users.add(user);

    //Act
    when(userRepository.findAll()).thenReturn(users);
    List<UserListResponse> userList = employeeService.viewAllEmployees();

    //Assert
    assertEquals(1, userList.size());
    assertEquals("John", userList.get(0).getName());
  }

  @Test
  void testChangePassword_success() throws Exception{
    ChangePasswordRequest request = new ChangePasswordRequest("1234", "12345678");
    when(authentication.getPrincipal()).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);

    String result = employeeService.changePassword(request);

    assertEquals("Password changed successfully", result);
    verify(userRepository).save(user);
  }

  @Test
  void testChangePassword_failure() {
    ChangePasswordRequest request = new ChangePasswordRequest("wrongpassword", "12345678");

    when(authentication.getPrincipal()).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);


    assertThrows(BadCredentialsException.class, () -> employeeService.changePassword(request));
  }

  @Test
  void testSkillsList() {
    List<Skill> skills = new ArrayList<>();
    skills.add(skill);

    when(skillRepository.findAll()).thenReturn(skills);
    List<Skill> skillList = employeeService.skillsList();

    assertEquals(1, skillList.size());
    assertEquals("Java", skillList.get(0).getSkillName());
  }

  @Test
  void addSkill() {
    String newSkill = "Java";

    when(authentication.getPrincipal()).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(employeeRepository.findById(user.getUserId())).thenReturn(Optional.of(employee));
    when(skillRepository.findBySkillName(newSkill)).thenReturn(skill);

    String result = employeeService.addSkill(newSkill);

    assertEquals("Added new skill successfully", result);
    verify(employeeSkillRepository).save(any(EmployeeSkill.class));
  }

  @Test
  void getProfile() {
    List<String> skills = Arrays.asList("Java", "Spring");

    when(authentication.getPrincipal()).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(employeeRepository.findById(user.getUserId())).thenReturn(Optional.of(employee));
    when(employeeSkillRepository.findSkillsById(user.getUserId())).thenReturn(skills);

    ProfileResponse result = employeeService.getProfile();

    assertEquals("abc@gmail.com", result.getEmail());
    assertEquals("Quiz Application", result.getProjectName());
    assertEquals(2, result.getSkills().size());
  }
}