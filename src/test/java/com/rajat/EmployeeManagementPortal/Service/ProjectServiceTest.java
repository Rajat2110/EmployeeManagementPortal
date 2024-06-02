package com.rajat.EmployeeManagementPortal.Service;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.ProjectRepository;
import com.rajat.EmployeeManagementPortal.request.CreateProjectRequest;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
  void testGetAllProjects() throws ParseException {
    LocalDate date = LocalDate.parse("1994-12-12");
    List<ProjectListResponse> projects = new ArrayList<>();
    projects.add(new ProjectListResponse(201L, "Quiz Application", date, 101L, "John"));

    Mockito.when(projectRepository.findAllProjectDetails()).thenReturn(projects);

    List<ProjectListResponse> response = projectService.allProjects();

    assertEquals(1, response.size());
    assertEquals("Quiz Application", response.get(0).getProjectName());
    assertEquals(101L, response.get(0).getManagerId());
  }

  @Test
  void testCreateProject() throws ParseException {
    Manager manager = Manager.builder()
      .userId(101L)
      .build();

    Mockito.when(projectRepository.save(project)).thenReturn(project);
    Mockito.when(managerRepository.findById(101L)).thenReturn(Optional.of(manager));

    LocalDate date = LocalDate.parse("1994-12-12");
    CreateProjectRequest request = new CreateProjectRequest("ERP Software", date, 101L);
    String response = projectService.createProject(request);

    assertEquals("Project Created", response);
  }

  @Test
  void testAssignProject() {
    Employee employee = Employee.builder()
      .userId(1L)
      .build();

    Mockito.when(projectRepository.findByProjectName("ERP Software")).thenReturn(Optional.of(project));
    Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

    String result = projectService.assignProject(1L, "ERP Software");

    assertEquals("Project assigned to the employee", result);
    assertEquals(project, employee.getProject());
  }

  @Test
  void testUnassignProject() {
    Employee employee = Employee.builder()
      .userId(1L)
      .build();

    Mockito.when(projectRepository.findByProjectName("ERP Software")).thenReturn(Optional.of(project));
    Mockito.when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

    String result = projectService.unassignProject(1L);

    assertEquals("Unassigned Employee from the project successfully", result);
  }
}