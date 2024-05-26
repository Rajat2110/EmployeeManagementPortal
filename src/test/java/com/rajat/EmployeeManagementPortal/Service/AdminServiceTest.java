package com.rajat.EmployeeManagementPortal.Service;

import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.STATUS;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.AdminService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class AdminServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private SkillRepository skillRepository;

  @Mock
  private RequestRepository requestRepository;
  AutoCloseable autoCloseable;
  User user;

  @InjectMocks
  private AdminService adminService;

  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
    user = new User(Long.parseLong("1"), "abc@gmail.com", "1234", "John", 9876543210L, 'M', "12-12-1994", USER_ROLE.EMPLOYEE);
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

    List<UserListResponse> userList = adminService.viewAll();

    assertEquals(1, userList.size());
    assertEquals("abc@gmail.com", userList.get(0).getEmail());
  }

  @Test
  void testUpdateEmployee() {

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
    skill.setSkillName("SQL");

    when(skillRepository.save(any(Skill.class))).thenReturn(skill);

    String response = adminService.addNewSkill(skill);

    assertEquals("Added new skill to the list", response);
  }
}