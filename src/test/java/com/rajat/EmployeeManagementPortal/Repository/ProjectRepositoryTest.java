package com.rajat.EmployeeManagementPortal.Repository;

import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.repository.ProjectRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ProjectRepositoryTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void findProjectByNameTest() {
        Project project = Project.builder()
                .projectName("Quiz Application")
                .build();

        projectRepository.save(project);
        Project foundProject = projectRepository.findByProjectName(project.getProjectName()).get();

        Assertions.assertThat(foundProject).isNotNull();
        Assertions.assertThat(foundProject.getProjectName()).isEqualTo(project.getProjectName());
    }
}
