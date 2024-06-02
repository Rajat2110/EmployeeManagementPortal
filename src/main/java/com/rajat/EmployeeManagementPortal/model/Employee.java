package com.rajat.EmployeeManagementPortal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
        query = "SELECT e.user_id AS userId, " +
                "u.name AS name, " +
                "u.contact AS contact, " +
                "ARRAY(SELECT s.skill_name from employee_skill es LEFT JOIN skill s ON es.skillid = s.skillid where es.user_id = e.user_id) as skills, " +
                "p.project_name AS project " +
                "FROM employee e " +
                "JOIN public.user u ON e.user_id = u.user_id " +
                "LEFT JOIN project p ON e.project_id = p.id " +
                "LEFT JOIN employee_skill es ON e.user_id = es.user_id " +
                "LEFT JOIN skill s ON es.skillid = s.skillid " +
                "WHERE (:skillName IS NULL OR s.skill_name = :skillName) " +
                "AND (:unassigned IS FALSE OR e.project_id IS NULL) " +
                "GROUP BY e.user_id, u.name, u.contact, p.project_name",
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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "userId")
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonBackReference
    private Project project;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmployeeSkill> employeeSkills;
}
