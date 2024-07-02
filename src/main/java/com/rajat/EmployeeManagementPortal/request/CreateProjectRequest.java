package com.rajat.EmployeeManagementPortal.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class CreateProjectRequest {
    private String projectName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate createdAt;
    private Long managerId;

    public CreateProjectRequest(String projectName, LocalDate createdAt,
                                Long managerId) {
        this.projectName = projectName;
        this.createdAt = createdAt;
        this.managerId = managerId;
    }

    public CreateProjectRequest() {}

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
