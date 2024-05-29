package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.STATUS;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.response.AdminUserListResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class AdminService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private SkillRepository skillRepository;

  public List<AdminUserListResponse> viewAll() {

    try {
      List<User> userList = userRepository.findAll();
      List<AdminUserListResponse> usersToDisplay = new ArrayList<>();

      for (User user : userList) {
        AdminUserListResponse userOutput = AdminUserListResponse.builder()
          .userId(user.getUserId())
          .email(user.getEmail())
          .name(user.getName())
          .contact(user.getContact())
          .gender(user.getGender())
          .dateOfBirth(user.getDateOfBirth())
          .role(user.getRole())
          .build();
        usersToDisplay.add(userOutput);
      }
      return usersToDisplay;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String updateEmployee(User details) {
    User foundUser = userRepository.findByUserId(details.getUserId())
      .orElseThrow(() -> new NoSuchElementException("No user found with this id"));

    foundUser.setEmail(details.getEmail());
    foundUser.setName(details.getName());
    foundUser.setContact(details.getContact());
    foundUser.setRole(details.getRole());

    userRepository.save(foundUser);

    return "Updated employee details successfully.";
  }

  @Transactional
  public String deleteEmployee(Long id) {
    try {
      userRepository.deleteByUserId(id);
      return "Employee deleted successfully";
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("No user found with this id");
    }
  }

  public String approveRequest(Long id, STATUS status) {
    try {
      Request req = requestRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("Request not found with id: " + id));

      req.setStatus(status);
      requestRepository.save(req);

      return "Request handled successfully.";
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<com.rajat.EmployeeManagementPortal.model.Request> viewRequests() {
    try {
      List<Request> requests = requestRepository.findAll();
      return requests;
    } catch (Exception e) {
      throw new RuntimeException("Failed to retrieve requests");
    }
  }

  public String addNewSkill(String skill) {
    try {
      var newSkill = Skill.builder()
        .skillName(skill)
        .build();

      skillRepository.save(newSkill);
      return "Added new skill to the list";
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public List<Skill> allSkills() {
    try {
      return skillRepository.findAll();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
