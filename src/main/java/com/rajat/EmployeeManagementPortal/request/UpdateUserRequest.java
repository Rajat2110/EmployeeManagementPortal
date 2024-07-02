package com.rajat.EmployeeManagementPortal.request;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;

public class UpdateUserRequest {

    private Long userId;
    private String email;
    private String name;
    private Long contact;
    private USER_ROLE role;

    public UpdateUserRequest(Long userId, String email, String name, Long contact,
                             USER_ROLE role) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.contact = contact;
        this.role = role;
    }

    public UpdateUserRequest() {
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

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }
}
