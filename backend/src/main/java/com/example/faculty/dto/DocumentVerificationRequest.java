package com.example.faculty.dto;

import jakarta.validation.constraints.NotBlank;
<<<<<<< HEAD
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocumentVerificationRequest {

    @NotBlank(message = "Rejection reason is required")
    private String reason;
}
=======

public record DocumentVerificationRequest(
        @NotBlank(message = "Reason is required when rejecting a document") String reason
) {}
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
