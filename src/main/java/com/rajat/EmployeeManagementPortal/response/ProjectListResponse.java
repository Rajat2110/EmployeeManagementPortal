package com.rajat.EmployeeManagementPortal.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProjectListResponse {

  private Long id;
  private String projectName;
  private Long managerId;
  private String managerName;

}
