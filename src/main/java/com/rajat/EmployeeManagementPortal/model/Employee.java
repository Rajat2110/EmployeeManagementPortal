package com.rajat.EmployeeManagementPortal.model;

import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NamedNativeQuery(name = "Employee.findAllEmployeeDetails",
  query = "SELECT e.user_id as userId, u.name as name, " +
    " ARRAY(SELECT s.skill_name from employee_skill es LEFT JOIN skill s ON es.skillid = s.skillid where es.user_id = e.user_id) as skills," +
    " p.project_name as project " +
    " FROM employee e " +
    " JOIN public.user u ON e.user_id = u.user_id" +
    " LEFT JOIN project p ON e.project_id = p.id",
  resultSetMapping = "Response.EmployeeListResponse")

@SqlResultSetMapping(name = "Response.EmployeeListResponse",
  classes = @ConstructorResult(targetClass = EmployeeListResponse.class,
    columns = {@ColumnResult(name = "userId"),
               @ColumnResult(name = "name"),
               @ColumnResult(name = "skills"),
               @ColumnResult(name = "project")}))

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employee")
public class Employee {

  @Id
  private Long userId;

  @OneToOne(fetch = FetchType.LAZY) @MapsId
  @JoinColumn(name = "userId")
  private User user;

  @ManyToOne
  private Project project;

  @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<EmployeeSkill> employeeSkills;
}
