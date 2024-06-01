package com.rajat.EmployeeManagementPortal.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajat.EmployeeManagementPortal.Config.JwtService;
import com.rajat.EmployeeManagementPortal.controller.AuthController;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.request.LoginRequest;
import com.rajat.EmployeeManagementPortal.response.AuthenticationResponse;
import com.rajat.EmployeeManagementPortal.response.ProfileResponse;
import com.rajat.EmployeeManagementPortal.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationService authenticationService;

  @MockBean
  private JwtService jwtService;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @WithMockUser(username = "abc@example.com", password = "12345678")
  void testLogin_success() throws Exception {
    LoginRequest request = new LoginRequest("abc@example.com", "12345678");
    AuthenticationResponse response = new AuthenticationResponse("jwtToken", USER_ROLE.EMPLOYEE);

    when(authenticationService.login(request)).thenReturn(response);

    mockMvc.perform(post("/auth/login").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(request)))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.jwt").value("jwtToken"))
      .andExpect(jsonPath("$.role").value("EMPLOYEE"));
  }

  @Test
  @WithMockUser(username = "abc@example.com", password = "12345678")
  void testLogin_failure() throws Exception {
    LoginRequest loginRequest = new LoginRequest("abc@example.com", "wrongpassword");

    when(authenticationService.login(loginRequest))
      .thenThrow(new BadCredentialsException("Invalid email or password"));

    mockMvc.perform(post("/auth/login").with(csrf())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)))
      .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(username = "abc@example.com", password = "12345678")
  void testGetUserProfile() throws Exception {
    ProfileResponse profile = ProfileResponse.builder()
      .email("abc@example.com")
      .name("John")
      .role(USER_ROLE.EMPLOYEE)
      .build();

    when(authenticationService.userProfile()).thenReturn(profile);

    mockMvc.perform(get("/auth/userProfile").with(csrf()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$.email").value("abc@example.com"))
      .andExpect(jsonPath("$.role").value("EMPLOYEE"));
  }
}
