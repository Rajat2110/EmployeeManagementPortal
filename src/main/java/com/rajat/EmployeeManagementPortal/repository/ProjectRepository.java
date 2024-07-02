package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByProjectName(String projectName);

    @Query("SELECT new com.rajat.EmployeeManagementPortal.response.ProjectListResponse(p.id, p.projectName, p.createdAt, m.userId, m.name)" +
            " FROM Project p LEFT JOIN p.manager m")
    List<ProjectListResponse> findAllProjectDetails();

    @Query("SELECT new com.rajat.EmployeeManagementPortal.response.ProjectListResponse(p.id, p.projectName, p.createdAt, m.userId, m.name) " +
            "FROM Project p LEFT JOIN p.manager m WHERE m.userId=:managerId")
    List<ProjectListResponse> findByManagerUserId(
            @Param("managerId") Long managerId);

    @Query("SELECT new com.rajat.EmployeeManagementPortal.response.ProjectListResponse(p.id, p.projectName, p.createdAt, m.userId, m.name)" +
            "FROM Project p LEFT JOIN p.manager m where p.id = :id")
    ProjectListResponse findAssignedProject(@Param("id") Long projectId);
}
