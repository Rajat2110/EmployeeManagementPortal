package com.rajat.EmployeeManagementPortal.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    @ManyToOne
    private Project project;
}
