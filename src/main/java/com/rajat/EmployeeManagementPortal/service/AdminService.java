package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.STATUS;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.request.UpdateUserRequest;
import com.rajat.EmployeeManagementPortal.response.AdminUserListResponse;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Service class for Admin operations
 */
@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registers a new user and saves it to the appropriate table based on role
     * @param request The user details needed for registration
     * @return Success message upon successful registration
     */
    public String registerNewUser(@Valid User request) {
        try {
            //create a new user from the request fields
            var newUser = new User();
            newUser.setEmail(request.getEmail());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));
            newUser.setName(request.getName());
            newUser.setContact(request.getContact());
            newUser.setGender(request.getGender());
            newUser.setDateOfBirth(request.getDateOfBirth());
            newUser.setRole(request.getRole());

            userRepository.save(newUser);
            return "Registered new user successfully";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a list of all the users
     * @return List of users in AdminUserListResponse format
     */
    public List<AdminUserListResponse> viewAll() {
        try {
            //find all the users
            List<User> userList = userRepository.findAll();
            List<AdminUserListResponse> usersToDisplay = new ArrayList<>();

            //map the response to AdminListResponse dto
            for (User user : userList) {
                AdminUserListResponse userOutput = new AdminUserListResponse();
                userOutput.setUserId(user.getUserId());
                userOutput.setEmail(user.getEmail());
                userOutput.setName(user.getName());
                userOutput.setContact(user.getContact());
                userOutput.setGender(user.getGender());
                userOutput.setDateOfBirth(user.getDateOfBirth());
                userOutput.setRole(user.getRole());
                usersToDisplay.add(userOutput);
            }
            return usersToDisplay;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the details of an existing user
     * @param details The user details required for update
     * @return Success message upon successful update
     */
    public String updateEmployee(UpdateUserRequest details) {
        //find the user using userId
        User foundUser = userRepository.findByUserId(details.getUserId())
                .orElseThrow(() -> new NoSuchElementException(
                        "No user found with this id"));

        //set new values using the request details
        foundUser.setEmail(details.getEmail());
        foundUser.setName(details.getName());
        foundUser.setContact(details.getContact());
        foundUser.setRole(details.getRole());

        userRepository.save(foundUser);

        return "Updated employee details successfully.";
    }

    /**
     * Deletes a user using user id
     * @param id The user id of the user to be deleted
     * @return Success message upon deletion
     */
    @Transactional
    public String deleteEmployee(Long id) {
        try {
            //delete the user using their id
            userRepository.deleteByUserId(id);
            return "Employee deleted successfully";
        } catch (NoSuchElementException e) {
            //throw an exception if no user is found with the provided user id
            throw new NoSuchElementException("No user found with this id");
        }
    }

    /**
     * Approves or rejects a request based on the provided status
     * @param id The id of the request
     * @param status the status of request to be set
     * @return Success message upon successful handling of the request
     */
    public String approveRequest(Long id, STATUS status) {
        try {
            //check if the request actually exists
            Request req = requestRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException(
                            "Request not found with id: " + id));

            //update the request status based on the request
            req.setStatus(status);
            requestRepository.save(req);

            return "Request handled successfully.";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves all the requests
     * @return List of requests
     */
    public List<Request> viewRequests() {
        try {
            return requestRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve requests");
        }
    }

    /**
     * Adds a new skill to the skill repository
     * @param skill Name of the new skill to be added
     * @return Success message upon successful addition of the skill
     */
    public String addNewSkill(String skill) {
        //check if the skill already exists
        if (skillRepository.findBySkillNameIgnoreCase(skill) != null) {
            throw new IllegalArgumentException("This skill already exists");
        }

        //create new skill using the skill name from the request
        var newSkill = new Skill();
        newSkill.setSkillName(skill);

        //save the new skill
        skillRepository.save(newSkill);
        return "Added new skill to the list";
    }

    /**
     * Retrieves all the skills
     * @return List of skills
     */
    public List<Skill> allSkills() {
        try {
            //find all the skills
            return skillRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the list of users with role employee
     * @param skillName The skill name to filter employees
     * @param unassigned Whether to filter by unassigned employees
     * @return List of Employees in the EmployeeListResponse format
     */
    public List<EmployeeListResponse> allEmployees(String skillName,
                                                   boolean unassigned) {
        List<User> employees = userRepository.findEmployees(skillName, unassigned);
        return employees.stream().map(this::convertToEmployeeListResponse).collect(
                Collectors.toList());
    }

    private EmployeeListResponse convertToEmployeeListResponse(User user) {
        List<String> skills = user.getSkills().stream()
                .map(employeeSkill -> employeeSkill.getSkill().getSkillName())
                .toList();

        String[] skillsArray = skills.toArray(new String[0]);
        String projectName = user.getProject() != null ? user.getProject().getProjectName() : null;
        return new EmployeeListResponse(user.getUserId(), user.getName(), skillsArray, projectName);
    }
}
