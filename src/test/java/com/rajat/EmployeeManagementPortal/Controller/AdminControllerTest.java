package com.rajat.EmployeeManagementPortal.Controller;

import com.rajat.EmployeeManagementPortal.Config.JwtService;
import com.rajat.EmployeeManagementPortal.controller.AdminController;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.AdminService;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AdminService adminService;

  @MockBean
  private ProjectService projectService;

  private UserListResponse user1;
  private UserListResponse user2;
  private List<UserListResponse> userList;
  private String jwtToken;

  @BeforeEach
  void setUp() {

    jwtToken = MockJwtService.generateToken("admin", USER_ROLE.ADMIN);

    user1 = UserListResponse.builder()
        .email("john@gmail.com")
        .name("John")
        .contact(9039123456L)
        .gender('M')
        .dateOfBirth("12-12-1996")
        .role(USER_ROLE.EMPLOYEE)
        .build();

    user2 = UserListResponse.builder()
      .email("lily@gmail.com")
      .name("Lily")
      .contact(9039493120L)
      .gender('F')
      .dateOfBirth("30-10-1995")
      .role(USER_ROLE.MANAGER)
      .build();

    userList.add(user1);
    userList.add(user2);
  }

  @AfterEach
  void tearDown() {
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void viewAll() throws Exception {
    when(adminService.viewAll()).thenReturn(userList);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/admin/viewAll")
        .header("Authorization", "Bearer "+jwtToken))
        .andDo(print()).andExpect(status().isOk());
  }

  @Test
  void projects() {
  }

  @Test
  void createProject() {
  }

  @Test
  void assignProject() {
  }

  @Test
  void addNewSkill() {
  }

  @Test
  void viewRequests() {
  }

  @Test
  void approveRequest() {
  }

  @Test
  void updateEmployee() {
  }

  @Test
  void deleteEmployee() {
  }
}