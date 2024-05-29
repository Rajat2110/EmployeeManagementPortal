package com.rajat.EmployeeManagementPortal.response;

import com.rajat.EmployeeManagementPortal.model.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeListResponse {
  private Long userId;
  private String name;
  private String[] skills;
  private String project;
}
