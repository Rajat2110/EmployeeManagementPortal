package com.rajat.EmployeeManagementPortal.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajat.EmployeeManagementPortal.Config.JwtService;
import com.rajat.EmployeeManagementPortal.controller.AdminController;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.STATUS;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.request.CreateProjectRequest;
import com.rajat.EmployeeManagementPortal.response.AdminUserListResponse;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.AdminService;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc
class AdminControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AdminService adminService;

  @MockBean
  private ProjectService projectService;

  @MockBean
  private JwtService jwtService;

  @Autowired
  private ObjectMapper objectMapper;

  private Key key;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testRegisterNewUser() throws Exception {
    User newUser = new User();
    newUser.setEmail("abc@example.com");
    newUser.setPassword("password");
    newUser.setRole(USER_ROLE.ADMIN);
    when(adminService.registerNewUser(any(User.class))).thenReturn("Registered new user successfully");

    MvcResult result = mockMvc.perform(post("/admin/registerNewUser").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content("{\"email\":\"abc@example.com\",\"password\":\"password\",\"role\":\"ADMIN\"}"))
      .andExpect(status().isOk())
      .andExpect(content().string("Registered new user successfully"))
      .andReturn();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testViewAll() throws Exception {
    AdminUserListResponse user = new AdminUserListResponse(1L, "abc@example.com", "John", 9089564636L, 'M', "30/07/1998", USER_ROLE.EMPLOYEE);
    List<AdminUserListResponse> employees = new ArrayList<>();
    employees.add(user);

    when(adminService.viewAll()).thenReturn(employees);

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/viewAll").with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$[0].email").value("abc@example.com"))
      .andExpect(jsonPath("$[0].role").value("EMPLOYEE"))
      .andExpect(jsonPath("$.length()").value(employees.size()));

    verify(adminService, times(1)).viewAll();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testGetAllEmployees() throws Exception {
    EmployeeListResponse employee = EmployeeListResponse.builder()
      .name("John")
      .project("Quiz Application")
      .build();
    List<EmployeeListResponse> employeeList = new ArrayList<>();
    employeeList.add(employee);
    String skillName = "Java";
    boolean unassigned = false;
    when(adminService.allEmployees(skillName, unassigned)).thenReturn(employeeList);

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/allEmployees")
        .param("skillName", skillName)
        .param("unassigned", String.valueOf(unassigned)).with(csrf()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.length()").value(employeeList.size()));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testGetAllProjects() throws Exception {
    List<ProjectListResponse> projects = Arrays.asList(new ProjectListResponse(), new ProjectListResponse());

    when(projectService.allProjects()).thenReturn(projects);

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/projects").with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.length()").value(projects.size()));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testCreateProject_success() throws Exception {
    CreateProjectRequest request = CreateProjectRequest.builder()
      .projectName("Test Project")
      .managerId(101L)
      .build();

    when(projectService.createProject(request)).thenReturn("Project Created");
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(request);

    mockMvc.perform(MockMvcRequestBuilders.post("/admin/createProject").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
      .andExpect(status().isCreated())
      .andExpect(content().string("Project Created"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testCreateProject_failure() throws Exception {
    CreateProjectRequest request = CreateProjectRequest.builder()
      .projectName("Test Project")
      .managerId(101L)
      .build();

    when(projectService.createProject(request)).thenThrow(new IllegalArgumentException("Manager not found with specified id."));
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(request);

    mockMvc.perform(MockMvcRequestBuilders.post("/admin/createProject").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.message").value("Manager not found with specified id."));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testDeleteProject() throws Exception {
    Long id = 1L;
    when(projectService.deleteProject(id)).thenReturn("Project deleted successfully");

    mockMvc.perform(delete("/admin/deleteProject").param("id", String.valueOf(1L))
      .with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().string("Project deleted successfully"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAssignProject_success() throws Exception {
    Long empId = 1L;
    String projectName = "Test Project";

    when(projectService.assignProject(empId, projectName)).thenReturn("Project assigned to the employee");

    mockMvc.perform(MockMvcRequestBuilders.put("/admin/assignProject")
      .param("empId", String.valueOf(empId))
      .param("projectName", projectName).with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().string("Project assigned to the employee"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAssignProject_failure() throws Exception {
    Long empId = 1L;
    String projectName = "Test Project";

    when(projectService.assignProject(empId, projectName)).thenThrow(new NoSuchElementException("No employee found with this id"));

    mockMvc.perform(MockMvcRequestBuilders.put("/admin/assignProject")
        .param("empId", String.valueOf(empId))
        .param("projectName", projectName).with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().string("No employee found with this id"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testUnassignProject() throws Exception {
    Long empId = 1L;
    String projectName = "Test Project";

    when(projectService.unassignProject(empId, projectName)).thenReturn("Unassigned Employee from the project");

    mockMvc.perform(MockMvcRequestBuilders.put("/admin/unassignProject")
        .param("empId", String.valueOf(empId))
        .param("projectName", projectName).with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().string("Unassigned Employee from the project"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testAddNewSkill() throws Exception {
    String skill = "Java";

    when(adminService.addNewSkill(skill)).thenReturn("Added new skill to the list");

    mockMvc.perform(MockMvcRequestBuilders.post("/admin/newSkill/{skill}", skill).with(csrf()))
      .andExpect(status().isCreated())
      .andExpect(content().string("Added new skill to the list"));
  }

  @Test
  void testGetAllSkills() throws Exception {
    List<Skill> skills = Arrays.asList(new Skill(), new Skill());

    when(adminService.allSkills()).thenReturn(skills);

    mockMvc.perform(get("/admin/allSkills").with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.length()").value(skills.size()));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testViewAllRequests() throws Exception {
    List<Request> requests = Arrays.asList(new Request(), new Request());

    when(adminService.viewRequests()).thenReturn(requests);

    mockMvc.perform(get("/admin/viewRequests").with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.length()").value(requests.size()));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testApproveRequest() throws Exception {
    Long id = 1L;

    when(adminService.approveRequest(id, STATUS.APPROVED)).thenReturn("Handled request successfully");

    mockMvc.perform(put("/admin/approveRequest/{id}/{status}", id, STATUS.APPROVED).with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().string("Handled request successfully"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testRejectRequest() throws Exception {
    Long id = 1L;

    when(adminService.approveRequest(id, STATUS.REJECTED)).thenReturn("Handled request successfully");

    mockMvc.perform(put("/admin/approveRequest/{id}/{status}", id, STATUS.REJECTED).with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().string("Handled request successfully"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testUpdateUser_success() throws Exception {
    User user = new User();
    user.setUserId(1L);
    user.setEmail("abc@example.com");
    user.setName("John");
    user.setContact(1234567890L);
    user.setRole(USER_ROLE.MANAGER);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(user);

    when(adminService.updateEmployee(user)).thenReturn("Updated employee details successfully");

    mockMvc.perform(put("/admin/updateUser").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(jsonRequest))
      .andExpect(status().isOk())
      .andExpect(content().string("Updated employee details successfully"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testUpdateUser_userNotFound() throws Exception {
    User user = new User();
    user.setUserId(1L);
    user.setEmail("abc@example.com");
    user.setName("John");
    user.setContact(1234567890L);
    user.setRole(USER_ROLE.MANAGER);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonRequest = objectMapper.writeValueAsString(user);

    when(adminService.updateEmployee(user)).thenThrow(new NoSuchElementException("No user found with this id"));

    mockMvc.perform(put("/admin/updateUser").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(jsonRequest))
      .andExpect(status().isOk())
      .andExpect(content().string("No user found with this id"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void testDeleteUser() throws Exception {
    Long id = 1L;

    when(adminService.deleteEmployee(id)).thenReturn("deleted employee successfully");

    mockMvc.perform(delete("/admin/deleteUser/{id}", id).with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().string("deleted employee successfully"));
  }
}