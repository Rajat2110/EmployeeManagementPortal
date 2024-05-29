package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.EmployeeSkill;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.EmployeeSkillRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.request.ChangePasswordRequest;
import com.rajat.EmployeeManagementPortal.response.ProfileResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private SkillRepository skillRepository;

  @Autowired
  private EmployeeSkillRepository employeeSkillRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public List<UserListResponse> viewAllEmployees() {

    List<User> userList = userRepository.findAll();
    List<UserListResponse> usersToDisplay = new ArrayList<>();

    for (User user : userList) {
      UserListResponse userOutput = UserListResponse.builder()
        .email(user.getEmail())
        .name(user.getName())
        .role(user.getRole())
        .build();
      usersToDisplay.add(userOutput);
    }
    return usersToDisplay;
  }

  public String changePassword(ChangePasswordRequest request) throws Exception {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    if (request.getNewPassword().length() < 8) {
      throw new IllegalArgumentException("Password must be at least 8 characters");
    }
    if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      user.setPassword(passwordEncoder.encode(request.getNewPassword()));
      userRepository.save(user);
      return "Password changed successfully";
    } else {
      throw new BadCredentialsException("Password doesn't match with existing password!");
    }
  }

  public List<Skill> skillsList() {
    return skillRepository.findAll();
  }

  public String addSkill(String newSkill) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    Employee emp = employeeRepository.findById(user.getUserId()).get();
    Skill skill = skillRepository.findBySkillName(newSkill);

    var empSkill = EmployeeSkill.builder()
      .employee(emp)
      .skill(skill)
      .build();

    employeeSkillRepository.save(empSkill);
    return "Added new skill successfully";
  }

  public ProfileResponse getProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = (User) authentication.getPrincipal();

    Employee emp = employeeRepository.findById(user.getUserId()).get();
    Project project = emp.getProject();

    List<String> skillList = employeeSkillRepository.findSkillsById(user.getUserId());

    return ProfileResponse.builder()
      .email(user.getEmail())
      .name(user.getName())
      .contact(user.getContact())
      .gender(user.getGender())
      .dateOfBirth(user.getDateOfBirth())
      .role(user.getRole())
      .projectName(project.getProjectName())
      .skills(skillList)
      .build();
  }

}
