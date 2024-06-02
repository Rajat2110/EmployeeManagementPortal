package com.rajat.EmployeeManagementPortal.controller;

import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.request.ChangePasswordRequest;
import com.rajat.EmployeeManagementPortal.response.ProfileResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.EmployeeService;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProjectService projectService;

    @GetMapping("/viewAll")
    public ResponseEntity<List<UserListResponse>> viewAllEmployees() {
        return ResponseEntity.ok(employeeService.viewAllEmployees());
    }

    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(employeeService.getProfile());
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request) throws Exception {
        return ResponseEntity.ok(employeeService.changePassword(request));
    }

    @GetMapping("/skillsList")
    public ResponseEntity<List<Skill>> skillsList() {
        return ResponseEntity.ok(employeeService.skillsList());
    }

    @PostMapping("/addSkill")
    public ResponseEntity<String> addSkill(@RequestParam String skill) {
        return ResponseEntity.ok(employeeService.addSkill(skill));
    }
}
