package com.rajat.EmployeeManagementPortal.repository;

import com.rajat.EmployeeManagementPortal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmail(String email);

  void deleteByUserId(Long id);

  Optional<User> findByUserId(Long id);
}
