package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  @Query("SELECT e FROM Employee e WHERE e.project.id is null")
  Optional<List<Employee>> findUnassignedEmployees();

  @Query(nativeQuery = true)
  List<EmployeeListResponse> findAllEmployeeDetails();

}
