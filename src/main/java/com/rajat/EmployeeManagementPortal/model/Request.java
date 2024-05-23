package com.rajat.EmployeeManagementPortal.model;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Entity
@Data
@Builder
@Table(name = "resource")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String skill;

    private int employeesRequired;

    private String requestedBy;

    @Enumerated(EnumType.STRING)
    private STATUS status;

}
