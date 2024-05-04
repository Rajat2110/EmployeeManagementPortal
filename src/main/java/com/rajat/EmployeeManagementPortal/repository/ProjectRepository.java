package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
