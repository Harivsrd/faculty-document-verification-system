package com.example.faculty.dto;

import com.example.faculty.entity.QualificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
<<<<<<< HEAD
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
=======

import java.math.BigDecimal;

public record EducationRequest(
        @NotNull(message = "Qualification type is required") QualificationType qualificationType,
        @NotBlank(message = "Institution is required") String institution,
        String boardOrUniversity,
        String courseName,
        Integer yearOfPassing,
        BigDecimal percentageOrCgpa
) {}
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
