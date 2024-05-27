package com.rajat.EmployeeManagementPortal.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "employee")
public class Employee {

  @Id
  private Long userId;

  @MapsId
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "userId")
  private User user;
  @ManyToOne
  private Project project;

  @ManyToMany
  @JoinTable(name = "EmployeeSkill",
    joinColumns = @JoinColumn(name = "userId"),
    inverseJoinColumns = @JoinColumn(name = "skillId"))
  private List<Skill> skills;
}
