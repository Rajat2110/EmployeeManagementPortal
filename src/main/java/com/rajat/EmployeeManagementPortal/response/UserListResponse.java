package com.rajat.EmployeeManagementPortal.response;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserListResponse {

  private String email;
  private String name;
  private Long contact;
  private Character gender;
  private String dateOfBirth;
  private USER_ROLE role;

}
