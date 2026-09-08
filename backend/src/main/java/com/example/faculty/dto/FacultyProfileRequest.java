package com.example.faculty.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FacultyProfileRequest {

    @Size(max = 150)
    private String fullName;

    @Size(max = 20)
    private String phone;

    private LocalDate dateOfBirth;

    private String gender;

    @Size(max = 500)
    private String address;

    private String city;

    private String state;

    private String specialization;

    private String department;
}
