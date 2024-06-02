package com.rajat.EmployeeManagementPortal.Service;

import com.rajat.EmployeeManagementPortal.Config.JwtService;
import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.request.LoginRequest;
import com.rajat.EmployeeManagementPortal.request.RegisterRequest;
import com.rajat.EmployeeManagementPortal.response.AuthenticationResponse;
import com.rajat.EmployeeManagementPortal.response.ProfileResponse;
import com.rajat.EmployeeManagementPortal.service.AuthenticationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private EmployeeRepository employeeRepository;
  @Mock
  private ManagerRepository managerRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private JwtService jwtService;
  @Mock
  private AuthenticationManager authenticationManager;
  AutoCloseable closeable;
  @InjectMocks
  private AuthenticationService authenticationService;

  @BeforeEach
  void setUp() {
    closeable = MockitoAnnotations.openMocks(this);
  }

  @AfterEach
  void tearDown() throws Exception {
    closeable.close();
  }

  @Test
  void testRegisterUser() {
    LocalDate date = LocalDate.parse("1998-07-30");
    RegisterRequest request = new RegisterRequest("manager@example.com","password",1234567890L,"Manager",
      'M',date, USER_ROLE.MANAGER);

    User newUser = User.builder()
      .email(request.getEmail())
      .password("encodedPassword")
      .name(request.getName())
      .contact(request.getContact())
      .gender(request.getGender())
      .dateOfBirth(request.getDateOfBirth())
      .role(request.getRole())
      .build();

    when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
    when(userRepository.save(any(User.class))).thenReturn(newUser);
    when(jwtService.generateToken(newUser)).thenReturn("jwtToken");

    AuthenticationResponse response = authenticationService.register(request);

    assertEquals("jwtToken", response.getJwt());
    assertEquals(USER_ROLE.MANAGER, response.getRole());
    verify(managerRepository, times(1)).save(any(Manager.class));
  }

  @Test
  void testLogin_success() {
    LoginRequest request = new LoginRequest("abc@example.com","password");
    User user = User.builder().email("abc@example.com").password("encodedPassword").role(USER_ROLE.EMPLOYEE).build();

    when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
    when(jwtService.generateToken(user)).thenReturn("jwtToken");

    AuthenticationResponse response = authenticationService.login(request);

    assertEquals("jwtToken", response.getJwt());
    assertEquals(USER_ROLE.EMPLOYEE, response.getRole());
  }

  @Test
  void testLogin_badCredentials() {
    LoginRequest request = new LoginRequest("abc@example.com", "password");

    doThrow(new BadCredentialsException("Invalid email or password")).when(authenticationManager).authenticate(any(
      UsernamePasswordAuthenticationToken.class));

    assertThrows(RuntimeException.class, ()->authenticationService.login(request));
  }

  @Test
  void userProfile() {
    LocalDate date = LocalDate.parse("1998-07-30");
    User user = User.builder().email("user@example.com").name("User").contact(1234567890L).gender('F').dateOfBirth(date).role(USER_ROLE.EMPLOYEE).build();
    Authentication authentication = mock(Authentication.class);
    SecurityContext securityContext = mock(SecurityContext.class);

    when(authentication.getPrincipal()).thenReturn(user);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);

    ProfileResponse profileResponse = authenticationService.userProfile();

    assertEquals("user@example.com", profileResponse.getEmail());
    assertEquals(1234567890L, profileResponse.getContact());
    assertEquals("1995-05-05", profileResponse.getDateOfBirth());
    assertEquals(USER_ROLE.EMPLOYEE, profileResponse.getRole());
  }
}