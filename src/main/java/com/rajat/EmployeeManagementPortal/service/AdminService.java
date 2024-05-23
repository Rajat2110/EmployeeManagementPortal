package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.*;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private SkillRepository skillRepository;

    public List<UserListResponse> viewAll() {

        List<User> userList = userRepository.findAll();
        List<UserListResponse> usersToDisplay = new ArrayList<>();

        for(User user : userList) {
            UserListResponse userOutput = UserListResponse.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .contact(user.getContact())
                    .gender(user.getGender())
                    .dateOfBirth(user.getDateOfBirth())
                    .role(user.getRole())
                    .build();
            usersToDisplay.add(userOutput);
        }
        return usersToDisplay;
    }

    public String updateEmployee(Long id, User details) {
        Optional<User> optionalUser = userRepository.findByUserId(id);
        User updateUser = optionalUser.get();

        updateUser.setName(details.getName());
        updateUser.setContact(details.getContact());
        updateUser.setRole(details.getRole());

        userRepository.save(updateUser);

        return "Updated employee details successfully.";
    }

    public String deleteEmployee(Long id) {
        userRepository.deleteByUserId(id);
        return "Employee deleted successfully";
    }

    public String approveRequest(com.rajat.EmployeeManagementPortal.model.Request request) {
        Optional<com.rajat.EmployeeManagementPortal.model.Request> req = requestRepository.findById(request.getId());
        Request pendingRequest = req.get();

        pendingRequest.setStatus(request.getStatus());
        requestRepository.save(pendingRequest);

        return "Request handled successfully.";
    }

    public List<com.rajat.EmployeeManagementPortal.model.Request> viewRequests() {
        List<com.rajat.EmployeeManagementPortal.model.Request> requests = requestRepository.findAll();
        return requests;
    }

    public String addNewSkill(Skill skill) {
        var newSkill = Skill.builder()
                .skillName(skill.getSkillName())
                .build();

        skillRepository.save(newSkill);
        return "Added new skill to the list";
    }
}
