package com.rajat.EmployeeManagementPortal.Repository;

import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.ProjectRepository;
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

  @Test
  public void saveProjectTest() {
    Project project = Project.builder()
      .projectName("Quiz Application")
      .build();

    Project savedProject = projectRepository.save(project);
    assertThat(savedProject).isNotNull();
    assertThat(savedProject.getProjectName()).isEqualTo(project.getProjectName());
  }

  @Test
  public void findProjectByNameTest() {
    Project project = Project.builder()
      .projectName("Quiz Application")
      .build();

    projectRepository.save(project);
    Project foundProject = projectRepository.findByProjectName(project.getProjectName()).get();

    assertThat(foundProject).isNotNull();
    assertThat(foundProject.getProjectName()).isEqualTo(project.getProjectName());
  }

  @Test
  public void findAllProjectsTest() {
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
  public void findAllProjectDetailsTest() {
    User user = User.builder()
      .userId(101L)
      .name("John")
      .role(USER_ROLE.MANAGER)
      .build();

    Manager manager = Manager.builder()
      .userId(101L)
      .user(user)
      .build();

    Project project1 = Project.builder()
      .id(1L)
      .projectName("Quiz Application")
      .manager(manager)
      .build();

    Project project2 = Project.builder()
      .id(2L)
      .projectName("ML Project")
      .manager(manager)
      .build();

    List<ProjectListResponse> projects = projectRepository.findAllProjectDetails();
    System.out.println(projects);

    assertThat(projects).isNotNull();
  }
}
