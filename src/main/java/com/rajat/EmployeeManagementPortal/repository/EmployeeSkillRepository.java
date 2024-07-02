package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.EmployeeSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EmployeeSkillRepository
        extends JpaRepository<EmployeeSkill, Long> {

    @Query("SELECT es.skill.skillName FROM EmployeeSkill es WHERE es.user.userId = :id")
    List<String> findSkillsById(Long id);
}
