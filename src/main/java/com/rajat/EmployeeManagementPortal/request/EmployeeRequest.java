package com.rajat.EmployeeManagementPortal.request;

import com.rajat.EmployeeManagementPortal.model.STATUS;

public class EmployeeRequest {

    private Long id;

    private String skillName;

    private int numOfEmployees;

    private STATUS status = STATUS.PENDING;

    public EmployeeRequest(Long id, String skillName, int numOfEmployees,
                           STATUS status) {
        this.id = id;
        this.skillName = skillName;
        this.numOfEmployees = numOfEmployees;
        this.status = status;
    }

    public EmployeeRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getNumOfEmployees() {
        return numOfEmployees;
    }

    public void setNumOfEmployees(int numOfEmployees) {
        this.numOfEmployees = numOfEmployees;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
}
