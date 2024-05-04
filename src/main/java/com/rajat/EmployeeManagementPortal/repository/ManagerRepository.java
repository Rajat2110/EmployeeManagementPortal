package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {
}
