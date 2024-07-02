package com.rajat.EmployeeManagementPortal.response;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;

public class AuthenticationResponse {

    private String jwt;
    private USER_ROLE role;

    public AuthenticationResponse(String jwt, USER_ROLE role) {
        this.jwt = jwt;
        this.role = role;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public USER_ROLE getRole() {
        return role;
    }

    public void setRole(USER_ROLE role) {
        this.role = role;
    }
}
