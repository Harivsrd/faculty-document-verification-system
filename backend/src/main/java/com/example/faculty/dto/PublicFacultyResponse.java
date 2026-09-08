package com.example.faculty.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicFacultyResponse {
    private Long facultyId;
    private String fullName;
    private String department;
    private String specialization;
    private String city;
    private String state;
=======
import com.example.faculty.entity.FacultyProfile;

/** Safe, public-facing subset of a faculty profile. Never includes contact/private fields. */
public record PublicFacultyResponse(
        Long id,
        String fullName,
        String specialization,
        String department,
        String city,
        String state
) {
    public static PublicFacultyResponse from(FacultyProfile p) {
        return new PublicFacultyResponse(
                p.getId(), p.getFullName(), p.getSpecialization(), p.getDepartment(), p.getCity(), p.getState()
        );
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
