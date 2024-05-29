package com.rajat.EmployeeManagementPortal.Service;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.ProjectRepository;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProjectServiceTest {

  @Mock
  private ProjectRepository projectRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private ManagerRepository managerRepository;

  @InjectMocks
  private ProjectService projectService;

  AutoCloseable autoCloseable;
  Project project;

  @BeforeEach
  void setUp() {
    autoCloseable = MockitoAnnotations.openMocks(this);
    project = Project.builder()
      .id(1L)
      .projectName("ERP Software")
      .build();
  }

  @AfterEach
  void tearDown() throws Exception {
    autoCloseable.close();
  }

  @Test
  void allProjects() {
    List<ProjectListResponse> projects = new ArrayList<>();
    projects.add(new ProjectListResponse(201L, "Quiz Application", 101L, "John"));

    Mockito.when(projectRepository.findAllProjectDetails()).thenReturn(projects);

    List<ProjectListResponse> response = projectService.allProjects();

    assertEquals(1, response.size());
    assertEquals("Quiz Application", response.get(0).getProjectName());
    assertEquals(101L, response.get(0).getManagerId());
  }

  @Test
  void createProjectTest() {
    Manager manager = Manager.builder()
      .userId(101L)
      .build();

    Mockito.when(projectRepository.save(project)).thenReturn(project);
    Mockito.when(managerRepository.findById(101L)).thenReturn(Optional.of(manager));

    String response = projectService.createProject("ERP Software", 101L);

    assertEquals("Project Created", response);
  }

  @Test
  void assignProject() {
    Employee employee = Employee.builder()
      .userId(1L)
      .build();

    Mockito.when(projectRepository.findByProjectName("ERP Software")).thenReturn(Optional.of(project));
    Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

    String result = projectService.assignProject(1L, "ERP Software");

    assertEquals("Project assigned to the employee", result);
    assertEquals(project, employee.getProject());
  }
}