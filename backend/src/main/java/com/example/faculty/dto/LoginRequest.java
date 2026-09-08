package com.example.faculty.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
<<<<<<< HEAD
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
=======

public record LoginRequest(
        @NotBlank @Email String email,
        @NotBlank String password
) {}
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
