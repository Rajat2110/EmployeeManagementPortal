package com.rajat.EmployeeManagementPortal.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajat.EmployeeManagementPortal.Config.JwtService;
import com.rajat.EmployeeManagementPortal.controller.ManagerController;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.request.ChangePasswordRequest;
import com.rajat.EmployeeManagementPortal.request.EmployeeRequest;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.ManagerService;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ManagerController.class)
@AutoConfigureMockMvc
class ManagerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ManagerService managerService;

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
  @WithMockUser(roles = "MANAGER")
  void testViewAll() throws Exception {
    UserListResponse user = new UserListResponse("abc@example.com", "John", 9089564636L, 'M', "30/07/1998", USER_ROLE.EMPLOYEE);
    List<UserListResponse> employees = new ArrayList<>();
    employees.add(user);

    when(managerService.viewAll()).thenReturn(employees);

    mockMvc.perform(MockMvcRequestBuilders.get("/manager/viewAll").with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$[0].email").value("abc@example.com"))
      .andExpect(jsonPath("$[0].role").value("EMPLOYEE"))
      .andExpect(jsonPath("$.length()").value(employees.size()));

    verify(managerService, times(1)).viewAll();
  }

  @Test
  @WithMockUser(roles = "MANAGER")
  void testGetAllEmployees() throws Exception {
    EmployeeListResponse employee = EmployeeListResponse.builder()
      .name("John")
      .project("Quiz Application")
      .build();
    List<EmployeeListResponse> employeeList = new ArrayList<>();
    employeeList.add(employee);
    String skillName = "Java";
    boolean unassigned = false;
    when(managerService.getAllEmployees(skillName, unassigned)).thenReturn(employeeList);

    mockMvc.perform(MockMvcRequestBuilders.get("/manager/employees")
      .param("skillName", skillName)
      .param("unassigned", String.valueOf(unassigned)).with(csrf()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(employeeList.size()));
  }

  @Test
  @WithMockUser(roles = "MANAGER")
  void testGetProjects() throws Exception {
    List<ProjectListResponse> projectList = Arrays.asList(new ProjectListResponse(), new ProjectListResponse());
    Long id = 1L;

    when(projectService.managedProjects(id)).thenReturn(projectList);

    mockMvc.perform(MockMvcRequestBuilders.get("/manager/myProjects/{id}", id))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(projectList.size()));
  }

  @Test
  @WithMockUser(roles = "MANAGER")
  void testRequestEmployee() throws Exception {
    EmployeeRequest employeeRequest = new EmployeeRequest();
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(employeeRequest);

    when(managerService.requestEmployee(employeeRequest)).thenReturn("Request submitted successfully");

    mockMvc.perform(MockMvcRequestBuilders.post("/manager/request").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(jsonRequest))
      .andExpect(status().isOk())
      .andExpect(content().string("Request submitted successfully"));
  }

  @Test
  @WithMockUser(roles = "MANAGER")
  void changePassword_success() throws Exception {
    ChangePasswordRequest request = new ChangePasswordRequest("password", "newPassword");
    when(managerService.changePassword(request)).thenReturn("Password changed successfully");

    mockMvc.perform(MockMvcRequestBuilders.put("/manager/changePassword").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content("{\"password\": \"password\", \"newPassword\": \"newPassword\"}"))
      .andExpect(status().isOk())
      .andExpect(content().string("Password changed successfully"));
  }
}