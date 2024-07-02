package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Request;
import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.repository.RequestRepository;
import com.rajat.EmployeeManagementPortal.repository.UserRepository;
import com.rajat.EmployeeManagementPortal.request.ChangePasswordRequest;
import com.rajat.EmployeeManagementPortal.request.EmployeeRequest;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import com.rajat.EmployeeManagementPortal.response.UserListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Service class for manager operations
 */
@Service
public class ManagerService {

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Retrieves the list of all the employees
     * @return List of all the employees in UserListResponse format
     */
    public List<UserListResponse> viewAll() {
        List<User> userList = userRepository.findAll();
        List<UserListResponse> usersToDisplay = new ArrayList<>();

        //map the found user list to UserListResponse dto
        for (User user : userList) {
            UserListResponse userOutput = new UserListResponse();
            userOutput.setEmail(user.getEmail());
            userOutput.setName(user.getName());
            userOutput.setGender(user.getGender());
            userOutput.setDateOfBirth(user.getDateOfBirth());
            userOutput.setRole(user.getRole());

            usersToDisplay.add(userOutput);
        }
        return usersToDisplay;
    }

    /**
     * Makes a request for employees
     * @param request Request containing details of the request in EmployeeRequest format
     * @return Success message upon successful request
     */
    public String requestEmployee(EmployeeRequest request) {
        //check if the manager actually exists
        User user = userRepository.findByUserId(request.getId())
                .orElseThrow(() -> new NoSuchElementException(
                        "No user found with provided id"));
        String name = user.getName();

        //create a new request based on the details in the request
        Request employeeRequest = new Request();
        employeeRequest.setSkill(request.getSkillName());
        employeeRequest.setRequestedBy(name);
        employeeRequest.setEmployeesRequired(request.getNumOfEmployees());
        employeeRequest.setStatus(request.getStatus());

        requestRepository.save(employeeRequest);
        return "Request submitted successfully.";
    }

    /**
     * Retrieves list of all the users with role employee in EmployeeListResponse format
     * @param skillName The skill name to filter employees
     * @param unassigned To filter only unassigned employees
     * @return List of all the employees with filter (if any)
     */
    public List<EmployeeListResponse> getAllEmployees(String skillName,
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

    /**
     * Changes or updates the password
     * @param request Request containing current and new password
     * @return Success message on successful password change
     * @throws Exception When current password does not match with the one in request
     */
    public String changePassword(ChangePasswordRequest request)
            throws Exception {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        //check if the password is 8 characters in length
        if (request.getNewPassword().length() < 8) {
            throw new IllegalArgumentException(
                    "Password must be at least 8 characters");
        }

        //check if the password provided in the request matches current password
        if (passwordEncoder.matches(request.getPassword(),
                user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return "Password changed successfully";
        } else {
            //if the password does not match throw exception
            throw new BadCredentialsException(
                    "Password doesn't match with existing password!");
        }
    }

    /**
     * Retrieves the list of all requests made by a manager
     * @return The List of requests made by the manager
     */
    public List<Request> myRequests() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        return requestRepository.findByRequestedBy(user.getName());
    }
}
