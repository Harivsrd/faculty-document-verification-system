package com.example.faculty.dto;

import com.example.faculty.entity.QualificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationRequest {

    @NotNull(message = "Qualification type is required")
    private QualificationType qualificationType;

    @NotBlank(message = "Institution is required")
    private String institution;

    private String boardOrUniversity;

    private String courseName;

    private Integer yearOfPassing;

    private Double percentageOrCgpa;
}
