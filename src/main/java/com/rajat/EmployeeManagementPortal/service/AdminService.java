package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.STATUS;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

  public List<UserListResponse> viewAll() {

    try {
      List<User> userList = userRepository.findAll();
      List<UserListResponse> usersToDisplay = new ArrayList<>();

      for (User user : userList) {
        UserListResponse userOutput = UserListResponse.builder()
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

  public String updateEmployee(Long id, User details) {
    try {
      Optional<User> optionalUser = userRepository.findByUserId(id);
      User updateUser = optionalUser.get();

      updateUser.setEmail(details.getEmail());
      updateUser.setName(details.getName());
      updateUser.setContact(details.getContact());
      updateUser.setRole(details.getRole());

      userRepository.save(updateUser);

      return "Updated employee details successfully.";
    } catch (NoSuchElementException e) {
      throw new NoSuchElementException("User not found with this id");
    }
  }

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

  public String addNewSkill(Skill skill) {
    try {
      var newSkill = Skill.builder()
        .skillName(skill.getSkillName())
        .build();

      skillRepository.save(newSkill);
      return "Added new skill to the list";
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
