package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.Config.JwtService;
import com.rajat.EmployeeManagementPortal.model.Employee;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ManagerRepository managerRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private JwtService jwtService;

  @Autowired
  private AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    var newUser = User.builder()
      .email(request.getEmail())
      .password(passwordEncoder.encode(request.getPassword()))
      .name(request.getName())
      .contact(request.getContact())
      .gender(request.getGender())
      .dateOfBirth(request.getDateOfBirth())
      .role(request.getRole())
      .build();

    var savedUser = userRepository.save(newUser);

    if (newUser.getRole() == USER_ROLE.MANAGER) {
      Manager manager = Manager.builder()
        .user(newUser)
        .build();
      managerRepository.save(manager);
    } else if (newUser.getRole() == USER_ROLE.EMPLOYEE) {
      Employee employee = Employee.builder()
        .user(newUser)
        .build();
      employeeRepository.save(employee);
    }

    var jwtToken = jwtService.generateToken(newUser);
    return AuthenticationResponse.builder()
      .jwt(jwtToken)
      .role(savedUser.getRole())
      .build();
  }

  public AuthenticationResponse login(LoginRequest request) {
    try {
      authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
          request.getEmail(),
          request.getPassword()
        )
      );

      var user = userRepository.findByEmail(request.getEmail())
        .orElseThrow();
      var jwtToken = jwtService.generateToken(user);
      return AuthenticationResponse.builder()
        .jwt(jwtToken)
        .role(user.getRole())
        .build();
    } catch (AuthenticationException e) {
      throw new BadCredentialsException("Invalid email or password");

    } catch (NoSuchElementException e) {
      throw new RuntimeException("No user found");
    }
  }

  public ProfileResponse userProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    return ProfileResponse.builder()
      .email(user.getEmail())
      .name(user.getName())
      .contact(user.getContact())
      .gender(user.getGender())
      .role(user.getRole())
      .build();
  }
}

