package com.rajat.EmployeeManagementPortal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "manager")
public class Manager {

    @Id
    private Long id;
    private String email;

    @OneToMany(mappedBy = "manager")
    private List<Project> projects;
}
