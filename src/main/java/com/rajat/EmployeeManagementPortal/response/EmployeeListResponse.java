package com.rajat.EmployeeManagementPortal.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListResponse {
  private Long id;
  private String name;
  private Long contact;
  private List<String> skills;
  private String project;
}
