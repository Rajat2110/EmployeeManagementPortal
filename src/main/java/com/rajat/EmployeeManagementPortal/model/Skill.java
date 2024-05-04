package com.rajat.EmployeeManagementPortal.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long skillID;

    private String skillName;
}
