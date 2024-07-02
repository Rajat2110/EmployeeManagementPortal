package com.rajat.EmployeeManagementPortal.response;

import java.time.LocalDate;

public class ProjectListResponse {

    private Long id;
    private String projectName;
    private LocalDate createdAt;
    private Long managerId;
    private String managerName;

    public ProjectListResponse(Long id, String projectName, LocalDate createdAt,
                               Long managerId, String managerName) {
        this.id = id;
        this.projectName = projectName;
        this.createdAt = createdAt;
        this.managerId = managerId;
        this.managerName = managerName;
    }

    public ProjectListResponse() {
    }

    public Long getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public Long getManagerId() {
        return managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
}
