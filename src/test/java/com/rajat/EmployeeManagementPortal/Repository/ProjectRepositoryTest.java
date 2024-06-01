package com.rajat.EmployeeManagementPortal.Repository;

import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.ProjectRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProjectRepositoryTest {

  @Autowired
  private ProjectRepository projectRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ManagerRepository managerRepository;

  @Test
  public void testSaveProject() {
    Project project = Project.builder()
      .projectName("Quiz Application")
      .build();

    Project savedProject = projectRepository.save(project);
    assertThat(savedProject).isNotNull();
    assertThat(savedProject.getProjectName()).isEqualTo(project.getProjectName());
  }

  @Test
  public void testFindProjectByName() {
    Project project = Project.builder()
      .projectName("Quiz Application")
      .build();

    projectRepository.save(project);
    Project foundProject = projectRepository.findByProjectName(project.getProjectName()).get();

    assertThat(foundProject).isNotNull();
    assertThat(foundProject.getProjectName()).isEqualTo(project.getProjectName());
  }

  @Test
  public void testFindAllProjects() {
    Project project1 = Project.builder()
      .projectName("Quiz Application")
      .build();

    Project project2 = Project.builder()
      .projectName("ML Project")
      .build();

    projectRepository.save(project1);
    projectRepository.save(project2);
    List<Project> projects = projectRepository.findAll();

    assertThat(projects).isNotNull();
    assertThat(projects.size()).isEqualTo(2);
  }

  @Test
  public void testFindAllProjectDetails() {
    User user = User.builder()
      .email("abc@example.com")
      .password("password")
      .role(USER_ROLE.MANAGER)
      .build();
    User savedUser = userRepository.save(user);

    Manager manager = Manager.builder()
      .user(savedUser)
      .build();
    Manager savedManager = managerRepository.save(manager);

    Project project1 = Project.builder()
      .id(1L)
      .projectName("Quiz Application")
      .manager(savedManager)
      .build();

    Project project2 = Project.builder()
      .id(2L)
      .projectName("ML Project")
      .build();

    projectRepository.save(project1);
    projectRepository.save(project2);

    List<ProjectListResponse> projects = projectRepository.findAllProjectDetails();
    System.out.println(projects);

    assertThat(projects).isNotNull();
  }
}
