package com.rajat.EmployeeManagementPortal.response;

public class EmployeeListResponse {
    private Long userId;
    private String name;
    private String[] skills;
    private String project;

    public EmployeeListResponse() {
    }

    public EmployeeListResponse(Long userId, String name, String[] skills,
                                String project) {
        this.userId = userId;
        this.name = name;
        this.skills = skills;
        this.project = project;
    }

    public Long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String[] getSkills() {
        return skills;
    }

    public String getProject() {
        return project;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSkills(String[] skills) {
        this.skills = skills;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
