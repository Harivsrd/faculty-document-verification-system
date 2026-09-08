package com.example.faculty.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentVerificationRequest {

    @NotBlank(message = "Rejection reason is required")
    private String reason;
}
