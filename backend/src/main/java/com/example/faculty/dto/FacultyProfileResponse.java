package com.example.faculty.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyProfileResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String city;
    private String state;
    private String specialization;
    private String department;
    private String profilePhoto;
=======
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
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
