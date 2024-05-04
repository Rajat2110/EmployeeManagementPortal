package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.ProjectRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public List<User> viewAll() {
        return userRepository.findAll();
    }

    public Project createProject() {
        
    }
}
