package com.rajat.EmployeeManagementPortal.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillID;

    @Column(nullable = false)
    private String skillName;

    @OneToMany(mappedBy = "skill")
    private List<EmployeeSkill> employeeSkills;

    public Long getSkillID() {
        return skillID;
    }

    public void setSkillID(Long skillID) {
        this.skillID = skillID;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
}
