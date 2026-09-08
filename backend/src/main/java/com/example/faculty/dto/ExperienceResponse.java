package com.example.faculty.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceResponse {
    private Long id;
    private String organization;
    private String designation;
    private String department;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean currentlyWorking;
    private String description;
    private List<DocumentResponse> documents;
=======
import com.example.faculty.entity.Experience;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record ExperienceResponse(
        Long id,
        String organization,
        String designation,
        String department,
        LocalDate startDate,
        LocalDate endDate,
        boolean currentlyWorking,
        String description,
        List<DocumentResponse> certificates,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ExperienceResponse from(Experience e) {
        List<DocumentResponse> docs = e.getCertificateDocuments() == null ? List.of() :
                e.getCertificateDocuments().stream().map(DocumentResponse::from).toList();
        return new ExperienceResponse(
                e.getId(), e.getOrganization(), e.getDesignation(), e.getDepartment(),
                e.getStartDate(), e.getEndDate(), e.isCurrentlyWorking(), e.getDescription(),
                docs, e.getCreatedAt(), e.getUpdatedAt()
        );
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
