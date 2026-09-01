package com.example.faculty.dto;

import jakarta.validation.constraints.NotBlank;

public record DocumentVerificationRequest(
        @NotBlank(message = "Reason is required when rejecting a document") String reason
) {}
