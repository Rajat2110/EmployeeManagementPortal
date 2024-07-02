package com.rajat.EmployeeManagementPortal.response;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;

import java.time.LocalDate;
import java.util.List;

public class ProfileResponse {

    private String email;
    private String name;
    private Long contact;
    private Character gender;
    private LocalDate dateOfBirth;
    private USER_ROLE role;
    private String projectName;
    private List<String> skills;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public Long getContact() {
        return contact;
    }

    public Character getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public String getProjectName() {
        return projectName;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
