package com.rajat.EmployeeManagementPortal.Controller;

import com.rajat.EmployeeManagementPortal.Config.JwtService;
import com.rajat.EmployeeManagementPortal.controller.EmployeeController;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.request.ChangePasswordRequest;
import com.rajat.EmployeeManagementPortal.response.ProfileResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.EmployeeService;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.jboss.logging.MDC.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
class EmployeeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private EmployeeService employeeService;

  @MockBean
  private ProjectService projectService;

  @MockBean
  private JwtService jwtService;

  AutoCloseable autoCloseable;

  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = "EMPLOYEE")
  void testViewAllEmployees() throws Exception {
    UserListResponse user = new UserListResponse("abc@example.com", "John", 9089564636L, 'M', "30/07/1998", USER_ROLE.EMPLOYEE);
    List<UserListResponse> employees = new ArrayList<>();
    employees.add(user);

    when(employeeService.viewAllEmployees()).thenReturn(employees);

    mockMvc.perform(MockMvcRequestBuilders.get("/employee/viewAll").with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$[0].email").value("abc@example.com"))
      .andExpect(jsonPath("$[0].role").value("EMPLOYEE"))
        .andExpect(jsonPath("$.length()").value(employees.size()));

    verify(employeeService, times(1)).viewAllEmployees();
  }

  @Test
  @WithMockUser(username = "user", password = "password", roles = "EMPLOYEE")
  void testGetProfile() throws Exception {
    ProfileResponse profileResponse = ProfileResponse.builder().name("John").role(USER_ROLE.MANAGER).build();

    when(employeeService.getProfile()).thenReturn(profileResponse);

    mockMvc.perform(MockMvcRequestBuilders.get("/employee/profile").with(csrf()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value(profileResponse.getName()));
  }

  @Test
  @WithMockUser(roles = "EMPLOYEE")
  void testChangePassword_success() throws Exception {
    ChangePasswordRequest request = new ChangePasswordRequest("password", "newPassword");
    when(employeeService.changePassword(request)).thenReturn("Password changed successfully");

    mockMvc.perform(MockMvcRequestBuilders.put("/employee/changePassword").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"password\": \"password\", \"newPassword\": \"newPassword\"}"))
      .andExpect(status().isOk())
      .andExpect(content().string("Password changed successfully"));
  }

  @Test
  @WithMockUser(roles = "EMPLOYEE")
  void testChangePassword_failure() throws Exception {
    ChangePasswordRequest request = new ChangePasswordRequest("password", "newPassword");
    when(employeeService.changePassword(request)).thenThrow(new BadCredentialsException("Invalid email or password"));

    mockMvc.perform(MockMvcRequestBuilders.put("/employee/changePassword").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"password\": \"password\", \"newPassword\": \"newPassword\"}"))
      .andExpect(status().isUnauthorized())
      .andExpect(content().string("Invalid email or password"));
  }

  @Test
  @WithMockUser(roles = "EMPLOYEE")
  void testGetSkillsList() throws Exception {
    List<Skill> skills = new ArrayList<>();
    skills.add(new Skill());
    skills.add(new Skill());

    when(employeeService.skillsList()).thenReturn(skills);

    mockMvc.perform(MockMvcRequestBuilders.get("/employee/skillsList").with(csrf()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(skills.size()));
  }

  @Test
  @WithMockUser(roles = "EMPLOYEE")
  void testAddSkill() throws Exception {
    String skill = "Java";
    when(employeeService.addSkill(skill)).thenReturn("Added new skill successfully");

    mockMvc.perform(MockMvcRequestBuilders.post("/employee/addSkill/{skill}", skill).with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().string("Added new skill successfully"));
  }
}