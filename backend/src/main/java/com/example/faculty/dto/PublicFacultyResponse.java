package com.example.faculty.dto;

import com.example.faculty.entity.FacultyProfile;

/** Safe, public-facing subset of a faculty profile. Never includes contact/private fields. */
public record PublicFacultyResponse(
        Long id,
        String fullName,
        String specialization,
        String department,
        String city,
        String state
) {
    public static PublicFacultyResponse from(FacultyProfile p) {
        return new PublicFacultyResponse(
                p.getId(), p.getFullName(), p.getSpecialization(), p.getDepartment(), p.getCity(), p.getState()
        );
    }
}
