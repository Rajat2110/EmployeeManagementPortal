package com.rajat.EmployeeManagementPortal.request;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String email;
    private String password;
    private Long contact;
    private String name;
    private Character gender;
    private String dateOfBirth;
    private USER_ROLE role;
}
