package com.example.faculty.mapper;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.entity.Document;

public final class DocumentMapper {

    private DocumentMapper() {
    }

    public static DocumentResponse toResponse(Document document) {
        if (document == null) {
            return null;
        }
        return DocumentResponse.builder()
                .id(document.getId())
                .facultyId(document.getFaculty().getId())
                .facultyName(document.getFaculty().getFullName())
                .educationId(document.getEducation() != null ? document.getEducation().getId() : null)
                .experienceId(document.getExperience() != null ? document.getExperience().getId() : null)
                .documentType(document.getDocumentType())
                .fileName(document.getFileName())
                .contentType(document.getContentType())
                .fileSize(document.getFileSize())
                .status(document.getStatus())
                .uploadedAt(document.getUploadedAt())
                .verifiedAt(document.getVerifiedAt())
                .verifiedByName(document.getVerifiedBy() != null ? document.getVerifiedBy().getName() : null)
                .rejectionReason(document.getRejectionReason())
                .build();
    }
}
