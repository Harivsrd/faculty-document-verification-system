package com.example.faculty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicFacultyDetailResponse {
    private Long facultyId;
    private String fullName;
    private String department;
    private String specialization;
    private String city;
    private String state;
    private List<String> verifiedQualifications;
    private List<String> verifiedExperience;
}
