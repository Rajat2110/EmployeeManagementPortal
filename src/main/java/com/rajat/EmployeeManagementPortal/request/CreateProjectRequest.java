package com.rajat.EmployeeManagementPortal.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateProjectRequest {
    private String projectName;
    private Long managerId;
}
