package com.example.faculty.dto;

<<<<<<< HEAD
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
=======
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ExperienceRequest(
        @NotBlank(message = "Organization is required") String organization,
        @NotBlank(message = "Designation is required") String designation,
        String department,
        @NotNull(message = "Start date is required") LocalDate startDate,
        LocalDate endDate,
        boolean currentlyWorking,
        String description
) {
    @AssertTrue(message = "Start date cannot be after end date")
    public boolean isDateRangeValid() {
        if (currentlyWorking || endDate == null || startDate == null) {
            return true;
        }
        return !startDate.isAfter(endDate);
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
