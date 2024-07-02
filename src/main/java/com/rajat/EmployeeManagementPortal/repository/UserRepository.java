package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.User;
import com.rajat.EmployeeManagementPortal.response.EmployeeListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    void deleteByUserId(Long id);

    Optional<User> findByUserId(Long id);

    @Query("SELECT u FROM User u " +
            "LEFT JOIN u.project p " +
            "LEFT JOIN EmployeeSkill es ON u.userId = es.user.userId " +
            "LEFT JOIN Skill s ON es.skill.skillID = s.skillID " +
            "WHERE (:skillName IS NULL OR s.skillName = :skillName) " +
            "AND (:unassigned IS FALSE OR p IS NULL) " +
            "AND u.role = 'EMPLOYEE'")
    List<User> findEmployees(@Param("skillName") String skillName, @Param("unassigned") boolean unassigned);
}
