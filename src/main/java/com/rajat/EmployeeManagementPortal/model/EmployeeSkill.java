package com.rajat.EmployeeManagementPortal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class EmployeeSkill {

    @Id
    private Long id;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Skill skill;
}
