package com.rajat.EmployeeManagementPortal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String projectName;

    @ManyToOne
    private Manager manager;

    @OneToMany(mappedBy = "project")
    private List<Employee> employees;

}
