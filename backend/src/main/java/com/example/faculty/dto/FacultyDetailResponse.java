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
public class FacultyDetailResponse {
    private FacultyProfileResponse profile;
    private List<EducationResponse> education;
    private List<ExperienceResponse> experience;
    private List<DocumentResponse> documents;
}
