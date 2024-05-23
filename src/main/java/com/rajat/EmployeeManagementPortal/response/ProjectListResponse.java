package com.rajat.EmployeeManagementPortal.response;

import com.rajat.EmployeeManagementPortal.model.Manager;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectListResponse {

    private Long id;
    private String projectName;
    private Long managerId;
    private String managerName;

}
