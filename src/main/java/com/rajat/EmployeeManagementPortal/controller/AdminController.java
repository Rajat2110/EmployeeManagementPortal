package com.rajat.EmployeeManagementPortal.controller;

import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.STATUS;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.request.CreateProjectRequest;
import com.rajat.EmployeeManagementPortal.request.UpdateUserRequest;
import com.rajat.EmployeeManagementPortal.response.AdminUserListResponse;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import com.rajat.EmployeeManagementPortal.service.AdminService;
import com.rajat.EmployeeManagementPortal.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProjectService projectService;

    @PostMapping("/registerNewUser")
    public ResponseEntity<String> registerNewUser(
            @Valid @RequestBody User request) {
        return ResponseEntity.ok(adminService.registerNewUser(request));
    }

    @GetMapping("/viewAll")
    public ResponseEntity<List<AdminUserListResponse>> viewAll() {
        return ResponseEntity.ok(adminService.viewAll());
    }

    @GetMapping("/allEmployees")
    public ResponseEntity<List<EmployeeListResponse>> allEmployees(
            @RequestParam(required = false) String skillName,
            @RequestParam(defaultValue = "false") boolean unassigned) {
        return ResponseEntity.ok(
                adminService.allEmployees(skillName, unassigned));
    }

    @GetMapping("/projects")
    public ResponseEntity<List<ProjectListResponse>> projects() {
        return ResponseEntity.ok(projectService.allProjects());
    }

    @PostMapping("/createProject")
    public ResponseEntity<String> createProject(
            @RequestBody CreateProjectRequest request) {
        return new ResponseEntity<>(projectService.createProject(request),
                HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteProject")
    public ResponseEntity<String> deleteProject(@RequestParam Long id) {
        return ResponseEntity.ok(projectService.deleteProject(id));
    }

    @PutMapping("/assignProject")
    public ResponseEntity<String> assignProject(@RequestParam Long empId,
                                                @RequestParam
                                                String projectName) {
        return ResponseEntity.ok(
                projectService.assignProject(empId, projectName));
    }

    @PutMapping("/unassignProject")
    public ResponseEntity<String> unassignProject(@RequestParam Long empId) {
        return ResponseEntity.ok(projectService.unassignProject(empId));
    }

    @PostMapping("/newSkill")
    public ResponseEntity<String> addNewSkill(@RequestParam String skill) {
        return new ResponseEntity<>(adminService.addNewSkill(skill),
                HttpStatus.CREATED);
    }

    @GetMapping("/allSkills")
    public ResponseEntity<List<Skill>> allSkills() {
        return ResponseEntity.ok(adminService.allSkills());
    }

    @GetMapping("/viewRequests")
    public ResponseEntity<List<Request>> viewRequests() {
        return ResponseEntity.ok(adminService.viewRequests());
    }

    @PutMapping("/approveRequest/{id}/{status}")
    public ResponseEntity<String> approveRequest(@PathVariable Long id,
                                                 @PathVariable STATUS status) {
        return ResponseEntity.ok(adminService.approveRequest(id, status));
    }

    @PutMapping("/updateUser")
    public ResponseEntity<String> updateEmployee(@RequestBody
                                                 UpdateUserRequest details) {
        return ResponseEntity.ok(adminService.updateEmployee(details));
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.deleteEmployee(id));
    }
}
