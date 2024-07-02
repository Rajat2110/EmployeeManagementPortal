package com.rajat.EmployeeManagementPortal.request;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;

import java.time.LocalDate;

public class RegisterRequest {

    private String email;
    private String password;
    private Long contact;
    private String name;
    private Character gender;
    private LocalDate dateOfBirth;
    private USER_ROLE role;

    public RegisterRequest(String email, String password, Long contact,
                           String name,
                           Character gender, LocalDate dateOfBirth,
                           USER_ROLE role) {
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }
}
