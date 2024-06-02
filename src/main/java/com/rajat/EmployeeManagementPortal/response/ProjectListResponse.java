package com.rajat.EmployeeManagementPortal.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjectListResponse {

    private Long id;
    private String projectName;
    private LocalDate createdAt;
    private Long managerId;
    private String managerName;

}
