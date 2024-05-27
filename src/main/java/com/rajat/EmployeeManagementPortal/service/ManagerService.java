package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.EmployeeSkillRepository;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.request.EmployeeRequest;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private RequestRepository requestRepository;

  @Autowired
  private EmployeeSkillRepository employeeSkillRepository;

  @Autowired
  private SkillRepository skillRepository;

  @Autowired
  private UserRepository userRepository;

  public List<UserListResponse> viewAll() {
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

  public String requestEmployee(EmployeeRequest request) {
    try {
      Request employeeRequest = Request.builder()
        .skill(request.getSkillName())
//                    .requestedBy()
        .employeesRequired(request.getNumOfEmployees())
        .status(request.getStatus())
        .build();

      requestRepository.save(employeeRequest);
      return "Request submitted successfully.";
    } catch (Exception e) {
      return "Some error occurred. Failed to submit request.";
    }
  }

  public List<Employee> filterEmployees(String filter) {
    if (filter.equals("unassigned")) {
      List<Employee> empList = employeeRepository.findUnassignedEmployees().get();
      return empList;
    } else {
      Skill findSkill = skillRepository.findBySkillName(filter);
//            Long skillId = findSkill.getId()
      return new ArrayList<Employee>();
    }
  }

  public List<EmployeeListResponse> getAllEmployees() {
    List<EmployeeListResponse> employees = employeeRepository.findAllEmployees();
    return employees;
  }
}
