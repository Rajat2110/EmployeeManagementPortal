package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
