package com.example.faculty.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ExperienceRequest {

    @NotBlank(message = "Organization is required")
    private String organization;

    @NotBlank(message = "Designation is required")
    private String designation;

    private String department;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    private boolean currentlyWorking;

    private String description;
}
