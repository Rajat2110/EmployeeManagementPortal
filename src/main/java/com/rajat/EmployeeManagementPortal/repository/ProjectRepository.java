package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
  Optional<Project> findByProjectName(String projectName);

  @Query("SELECT new com.rajat.EmployeeManagementPortal.response.ProjectListResponse(p.id, p.projectName, p.manager.userId, p.manager.user.name) FROM Project p")
  List<ProjectListResponse> findAllProjectDetails();
}
