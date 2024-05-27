package com.rajat.EmployeeManagementPortal.controller;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.request.EmployeeRequest;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.ManagerService;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/manager")
public class ManagerController {

  @Autowired
  private ManagerService managerService;

  @Autowired
  private ProjectService projectService;

  @GetMapping("/viewAll")
  public ResponseEntity<List<UserListResponse>> viewAll() {
    return ResponseEntity.ok(managerService.viewAll());
  }

  @GetMapping("/employees")
  public ResponseEntity<List<EmployeeListResponse>> getAllEmployees() {
    return ResponseEntity.ok(managerService.getAllEmployees());
  }

  @GetMapping("/employees/{filter}")
  public ResponseEntity<List<Employee>> filterEmployees(@PathVariable String filter) {
    return ResponseEntity.ok(managerService.filterEmployees(filter));
  }

  @GetMapping("/projects")
  public ResponseEntity<List<ProjectListResponse>> projects() {
    return ResponseEntity.ok(projectService.allProjects());
  }

  @PostMapping("/request")
  public ResponseEntity<String> requestEmployee(@RequestBody EmployeeRequest request) {
    return ResponseEntity.ok(managerService.requestEmployee(request));
  }
}
