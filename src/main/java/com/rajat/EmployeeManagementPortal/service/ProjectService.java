package com.rajat.EmployeeManagementPortal.service;

import com.rajat.EmployeeManagementPortal.model.Employee;
import com.rajat.EmployeeManagementPortal.model.Manager;
import com.rajat.EmployeeManagementPortal.model.Project;
import com.rajat.EmployeeManagementPortal.repository.EmployeeRepository;
import com.rajat.EmployeeManagementPortal.repository.ManagerRepository;
import com.rajat.EmployeeManagementPortal.repository.ProjectRepository;
import com.rajat.EmployeeManagementPortal.request.CreateProjectRequest;
import com.rajat.EmployeeManagementPortal.response.ProjectListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Service class for project operations
 */
@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Retrieves a list of all projects
     * @return List of projects
     */
    public List<ProjectListResponse> allProjects() {
        return projectRepository.findAllProjectDetails();
    }

    /**
     * Creates a new project
     * @param request Contains project details for creating a new project
     * @return Success message upon successful project creation
     */
    public String createProject(CreateProjectRequest request) {
        if (projectRepository.findByProjectName(request.getProjectName())
                .isPresent()) {
            throw new IllegalArgumentException(
                    "Project with this name already exists!");
        }

        Manager manager = managerRepository.findById(request.getManagerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Manager not found with specified id."));
        var project = Project.builder()
                .projectName(request.getProjectName())
                .createdAt(request.getCreatedAt())
                .manager(manager)
                .build();

        projectRepository.save(project);
        return "Project Created";
    }

    /**
     * Assigns a project to an employee
     * @param empId The user id of the employee
     * @param projectName The name of the project which is to be assigned
     * @return Success message upon successful assignment of project
     */
    public String assignProject(Long empId, String projectName) {
        Project project = projectRepository.findByProjectName(projectName)
                .orElseThrow(() -> new NoSuchElementException(
                        "No project found with this name"));

        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No employee found with this id"));

        emp.setProject(project);
        employeeRepository.save(emp);
        return "Project assigned to the employee";
    }

    /**
     * Removes an employee from a project
     * @param empId The user id of the employee
     * @return Success message upon successful removal of employee from project
     */
    public String unassignProject(Long empId) {
        Employee emp = employeeRepository.findById(empId)
                .orElseThrow(() -> new NoSuchElementException(
                        "No employee found with this id"));

        emp.setProject(null);
        employeeRepository.save(emp);
        return "Unassigned Employee from the project successfully";
    }

    /**
     * Retrieves a list of projects managed by a manager
     * @param managerId The id of the manager
     * @return List of projects managed by the manager
     */
    public List<ProjectListResponse> managedProjects(Long managerId) {
        return projectRepository.findByManagerUserId(managerId);
    }

    /**
     * Deletes a project using the project id
     * @param id The id of the project
     * @return Success message upon successful deletion
     */
    public String deleteProject(Long id) {
        try {
            projectRepository.deleteById(id);
            return "Project deleted successfully";
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("No project exists with this id");
        }
    }
}
