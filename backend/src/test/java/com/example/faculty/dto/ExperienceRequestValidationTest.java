package com.example.faculty.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ExperienceRequestValidationTest {

    private final Validator validator;

    ExperienceRequestValidationTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    @Test
    void rejectsStartDateAfterEndDate() {
        ExperienceRequest request = new ExperienceRequest(
                "ABC College", "Lecturer", "CS",
                LocalDate.of(2023, 1, 1), LocalDate.of(2022, 1, 1),
                false, "desc"
        );

        Set<ConstraintViolation<ExperienceRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
    }

    @Test
    void allowsValidDateRange() {
        ExperienceRequest request = new ExperienceRequest(
                "ABC College", "Lecturer", "CS",
                LocalDate.of(2021, 1, 1), LocalDate.of(2023, 1, 1),
                false, "desc"
        );

        Set<ConstraintViolation<ExperienceRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void allowsMissingEndDateWhenCurrentlyWorking() {
        ExperienceRequest request = new ExperienceRequest(
                "XYZ University", "Assistant Professor", "CS",
                LocalDate.of(2023, 1, 1), null,
                true, "desc"
        );

        Set<ConstraintViolation<ExperienceRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }
}
