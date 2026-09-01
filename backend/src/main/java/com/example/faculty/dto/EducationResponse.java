package com.example.faculty.dto;

import com.example.faculty.entity.Education;
import com.example.faculty.entity.QualificationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EducationResponse(
        Long id,
        QualificationType qualificationType,
        String institution,
        String boardOrUniversity,
        String courseName,
        Integer yearOfPassing,
        BigDecimal percentageOrCgpa,
        List<DocumentResponse> certificates,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static EducationResponse from(Education e) {
        List<DocumentResponse> docs = e.getCertificateDocuments() == null ? List.of() :
                e.getCertificateDocuments().stream().map(DocumentResponse::from).toList();
        return new EducationResponse(
                e.getId(), e.getQualificationType(), e.getInstitution(), e.getBoardOrUniversity(),
                e.getCourseName(), e.getYearOfPassing(), e.getPercentageOrCgpa(), docs,
                e.getCreatedAt(), e.getUpdatedAt()
        );
    }
}
