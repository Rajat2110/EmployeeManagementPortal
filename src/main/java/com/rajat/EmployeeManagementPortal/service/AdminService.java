package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.STATUS;
import com.rajat.EmployeeManagementPortal.model.Skill;
import com.rajat.EmployeeManagementPortal.model.USER_ROLE;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.SkillRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
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

/**
 * Service class for Admin operations
 */
@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

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
    @Transactional
    public String registerNewUser(@Valid User request) {
        try {
            //create a new user from the request fields
            var newUser = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .contact(request.getContact())
                    .gender(request.getGender())
                    .dateOfBirth(request.getDateOfBirth())
                    .role(request.getRole())
                    .build();

            userRepository.save(newUser);

            //insert into manager or employee table according to  user role
            if (newUser.getRole() == USER_ROLE.MANAGER) {
                Manager manager = Manager.builder()
                        .user(newUser)
                        .build();
                managerRepository.save(manager);
            } else if (newUser.getRole() == USER_ROLE.EMPLOYEE) {
                Employee employee = Employee.builder()
                        .user(newUser)
                        .build();
                employeeRepository.save(employee);
            }
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
                AdminUserListResponse userOutput =
                        AdminUserListResponse.builder()
                                .userId(user.getUserId())
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the details of an existing user
     * @param details The user details required for update
     * @return Success message upon successful update
     */
    public String updateEmployee(User details) {
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
            List<Request> requests = requestRepository.findAll();
            return requests;
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
        try {
            //check if the skill already exists
            if (skillRepository.findBySkillName(skill) != null) {
                throw new IllegalArgumentException("This skill already exists");
            }

            //create new skill using the skill name from the request
            var newSkill = Skill.builder()
                    .skillName(skill)
                    .build();

            //save the new skill
            skillRepository.save(newSkill);
            return "Added new skill to the list";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        return employeeRepository.findAllEmployeeDetails(skillName, unassigned);
    }
}
