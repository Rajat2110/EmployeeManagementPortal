package com.rajat.EmployeeManagementPortal.response;

import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProfileResponse {

    private String email;
    private String name;
    private Long contact;
    private Character gender;
    private LocalDate dateOfBirth;
    private USER_ROLE role;
    private String projectName;
    private List<String> skills;
}
