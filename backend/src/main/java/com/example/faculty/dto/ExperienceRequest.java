package com.example.faculty.dto;

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
}
