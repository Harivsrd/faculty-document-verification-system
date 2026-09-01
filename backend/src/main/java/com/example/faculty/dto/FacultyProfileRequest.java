package com.example.faculty.dto;

import java.time.LocalDate;

public record FacultyProfileRequest(
        String fullName,
        String phone,
        LocalDate dateOfBirth,
        String gender,
        String address,
        String city,
        String state,
        String specialization,
        String department,
        Boolean publiclyVisible
) {}
