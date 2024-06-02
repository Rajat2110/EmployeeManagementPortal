package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query(nativeQuery = true)
    List<EmployeeListResponse> findAllEmployeeDetails(
            @Param("skillName") String skillName,
            @Param("unassigned") boolean unassigned);

}
