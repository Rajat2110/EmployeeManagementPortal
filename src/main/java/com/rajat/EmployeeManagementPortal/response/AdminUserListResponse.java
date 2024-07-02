package com.rajat.EmployeeManagementPortal.response;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;

import java.time.LocalDate;

public class AdminUserListResponse {
    private Long userId;
    private String email;
    private String name;
    private Long contact;
    private Character gender;
    private LocalDate dateOfBirth;
    private USER_ROLE role;

    public AdminUserListResponse(Long userId, String email, String name,
                                 Long contact, Character gender,
                                 LocalDate dateOfBirth, USER_ROLE role) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.contact = contact;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    public AdminUserListResponse() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getContact() {
        return contact;
    }

    public void setContact(Long contact) {
        this.contact = contact;
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
