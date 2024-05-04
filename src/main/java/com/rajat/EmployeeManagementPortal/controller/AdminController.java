package com.rajat.EmployeeManagementPortal.controller;

import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/home")
    public String adminHome() {
        return "this is admin home!";
    }

    @GetMapping("/viewAll")
    public ResponseEntity<List<User>> viewAll() {
        return ResponseEntity.ok(adminService.viewAll());
    }

    @PostMapping("/createProject")
    public ResponseEntity<String> createProject(@RequestBody Project project) {
        return ResponseEntity.ok(adminService.createProject(), HttpStatus.CREATED);
    }
}
