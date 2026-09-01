package com.example.faculty.dto;

import com.example.faculty.entity.Document;
import com.example.faculty.entity.DocumentStatus;
import com.example.faculty.entity.DocumentType;

import java.time.LocalDateTime;

public record DocumentResponse(
        Long id,
        Long facultyId,
        String facultyName,
        DocumentType documentType,
        Long educationId,
        Long experienceId,
        String fileName,
        String contentType,
        Long fileSize,
        DocumentStatus status,
        LocalDateTime uploadedAt,
        LocalDateTime verifiedAt,
        String verifiedByName,
        String rejectionReason
) {
    public static DocumentResponse from(Document d) {
        return new DocumentResponse(
                d.getId(),
                d.getFaculty() != null ? d.getFaculty().getId() : null,
                d.getFaculty() != null ? d.getFaculty().getFullName() : null,
                d.getDocumentType(),
                d.getEducation() != null ? d.getEducation().getId() : null,
                d.getExperience() != null ? d.getExperience().getId() : null,
                d.getFileName(),
                d.getContentType(),
                d.getFileSize(),
                d.getStatus(),
                d.getUploadedAt(),
                d.getVerifiedAt(),
                d.getVerifiedBy() != null ? d.getVerifiedBy().getName() : null,
                d.getRejectionReason()
        );
    }
}
