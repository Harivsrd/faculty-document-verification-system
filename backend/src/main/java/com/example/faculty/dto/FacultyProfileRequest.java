package com.example.faculty.dto;

<<<<<<< HEAD
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FacultyProfileRequest {

    @Size(max = 150)
    private String fullName;

    @Size(max = 20)
    private String phone;

    private LocalDate dateOfBirth;

    private String gender;

    @Size(max = 500)
    private String address;

    private String city;

    private String state;

    private String specialization;

    private String department;
}
=======
import java.time.LocalDate;

public record FacultyProfileRequest(
        String fullName,
        String phone,
        LocalDate dateOfBirth,
        String gender,
        String address,
        String city,
        String state,
        String specialization,
        String department,
        Boolean publiclyVisible
) {}
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
