package com.rajat.EmployeeManagementPortal.request;

import com.rajat.EmployeeManagementPortal.model.STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {

  private Long id;

  private String skillName;

  private int numOfEmployees;

  private STATUS status = STATUS.PENDING;
}
