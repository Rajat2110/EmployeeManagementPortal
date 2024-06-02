package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.EmployeeSkill;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.EmployeeSkillRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.request.ChangePasswordRequest;
import com.rajat.EmployeeManagementPortal.response.ProfileResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for employee operations
 */
@Service
public class EmployeeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private EmployeeSkillRepository employeeSkillRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieves a list of all employees
     * @return List of employees in UserListResponse format
     */
    public List<UserListResponse> viewAllEmployees() {

        List<User> userList = userRepository.findAll();
        List<UserListResponse> usersToDisplay = new ArrayList<>();

        for (User user : userList) {
            UserListResponse userOutput = UserListResponse.builder()
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .build();
            usersToDisplay.add(userOutput);
        }
        return usersToDisplay;
    }

    /**
     * Returns the profile of an employee
     * @return Profile in the ProfileResponse format
     */
    public ProfileResponse getProfile() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        Employee emp = employeeRepository.findById(user.getUserId()).get();
        Project project = emp.getProject();

        List<String> skillList =
                employeeSkillRepository.findSkillsById(user.getUserId());

        return ProfileResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .contact(user.getContact())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .projectName(project != null ? project.getProjectName() : null)
                .skills(skillList)
                .build();
    }

    /**
     * Changes the password for an employee
     * @param request Request containing current and new password fields
     * @return Success message upon successful password change
     * @throws Exception When the current password does not match with the one in the request
     */
    public String changePassword(ChangePasswordRequest request)
            throws Exception {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        if (request.getNewPassword().length() < 8) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters");
        }
        if (passwordEncoder.matches(request.getPassword(),
                user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return "Password changed successfully";
        } else {
            throw new BadCredentialsException(
                    "Password doesn't match with existing password!");
        }
    }

    /**
     * Retrieves the list of available skills
     * @return The list of skills
     */
    public List<Skill> skillsList() {
        return skillRepository.findAll();
    }

    /**
     * Adds a new skill to the employee's skillset
     * @param skill The name of the skill to be added
     * @return Success message upon successful skill addition
     */
    public String addSkill(String skill) {
        try {
            Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();

            Employee emp = employeeRepository.findById(user.getUserId()).get();
            Skill foundSkill = skillRepository.findBySkillName(skill);

            var empSkill = EmployeeSkill.builder()
                    .employee(emp)
                    .skill(foundSkill)
                    .build();

            employeeSkillRepository.save(empSkill);
            return "Added new skill successfully";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
