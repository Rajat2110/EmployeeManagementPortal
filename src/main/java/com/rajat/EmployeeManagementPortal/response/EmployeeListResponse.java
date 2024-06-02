package com.rajat.EmployeeManagementPortal.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListResponse {
    private Long userId;
    private String name;
    private String[] skills;
    private String project;
}
