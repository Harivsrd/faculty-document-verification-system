package com.example.faculty.dto;

import com.example.faculty.entity.QualificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EducationRequest(
        @NotNull(message = "Qualification type is required") QualificationType qualificationType,
        @NotBlank(message = "Institution is required") String institution,
        String boardOrUniversity,
        String courseName,
        Integer yearOfPassing,
        BigDecimal percentageOrCgpa
) {}
