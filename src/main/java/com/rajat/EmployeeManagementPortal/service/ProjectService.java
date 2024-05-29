package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.ProjectRepository;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ProjectService {

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private ManagerRepository managerRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  public List<ProjectListResponse> allProjects() {
    List<ProjectListResponse> projectList = projectRepository.findAllProjectDetails();
    return projectList;
  }

  public String createProject(String projectName, Long id) {
    Manager manager = managerRepository.findById(id)
      .orElseThrow(() -> new IllegalArgumentException("Manager not found with specified id."));
    var project = Project.builder()
      .projectName(projectName)
      .manager(manager)
      .build();

    var saveProject = projectRepository.save(project);
    return "Project Created";
  }

  public String assignProject(Long empId, String projectName) {
    Project project = projectRepository.findByProjectName(projectName)
      .orElseThrow(() -> new NoSuchElementException("No project found with this name"));

    Employee emp = employeeRepository.findById(empId)
        .orElseThrow(() -> new NoSuchElementException("No employee found with this id"));

    emp.setProject(project);
    employeeRepository.save(emp);
    return "Project assigned to the employee";
  }

  public String unassignProject(Long empId, String projectName) {
    Employee emp = employeeRepository.findById(empId)
      .orElseThrow(() -> new NoSuchElementException("No employee found with this id"));
    
    emp.setProject(null);
    return "Unassigned Employee from the project successfully";
  }
}
