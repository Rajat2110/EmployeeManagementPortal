package com.rajat.EmployeeManagementPortal.controller;

import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.response.ProfileResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import com.rajat.EmployeeManagementPortal.service.EmployeeService;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody String password, @RequestBody String newPassword) throws Exception {
        return ResponseEntity.ok(employeeService.changePassword(password, newPassword));
    }

    @PostMapping("/addSkill")
    public ResponseEntity<String> addSkill(@RequestBody Skill request) {
        return ResponseEntity.ok(employeeService.addSkill(request));
    }
}
