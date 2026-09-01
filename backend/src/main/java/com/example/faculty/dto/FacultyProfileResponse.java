package com.example.faculty.dto;

import com.example.faculty.entity.FacultyProfile;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FacultyProfileResponse(
        Long id,
        Long userId,
        String name,
        String email,
        String fullName,
        String phone,
        LocalDate dateOfBirth,
        String gender,
        String address,
        String city,
        String state,
        String specialization,
        String department,
        String profilePhoto,
        boolean publiclyVisible,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static FacultyProfileResponse from(FacultyProfile p) {
        return new FacultyProfileResponse(
                p.getId(),
                p.getUser().getId(),
                p.getUser().getName(),
                p.getUser().getEmail(),
                p.getFullName(),
                p.getPhone(),
                p.getDateOfBirth(),
                p.getGender(),
                p.getAddress(),
                p.getCity(),
                p.getState(),
                p.getSpecialization(),
                p.getDepartment(),
                p.getProfilePhoto(),
                p.isPubliclyVisible(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }
}
