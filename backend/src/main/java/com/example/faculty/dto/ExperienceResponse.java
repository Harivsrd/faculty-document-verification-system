package com.example.faculty.dto;

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
}
