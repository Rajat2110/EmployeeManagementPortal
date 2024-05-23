package com.rajat.EmployeeManagementPortal.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "employee")
public class Employee {

    @Id
    private Long userId;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User user;
    @ManyToOne
    private Project project;

    @ManyToMany
    @JoinTable(name = "EmployeeSkill",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "skillId"))
    private List<Skill> skills;
}
