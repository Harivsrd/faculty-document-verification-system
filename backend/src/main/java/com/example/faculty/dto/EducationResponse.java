package com.example.faculty.dto;

import com.example.faculty.entity.QualificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EducationResponse {
    private Long id;
    private QualificationType qualificationType;
    private String institution;
    private String boardOrUniversity;
    private String courseName;
    private Integer yearOfPassing;
    private Double percentageOrCgpa;
    private List<DocumentResponse> documents;
}
