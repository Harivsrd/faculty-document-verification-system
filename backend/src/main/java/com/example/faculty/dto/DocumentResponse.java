package com.example.faculty.dto;

<<<<<<< HEAD
import com.example.faculty.entity.DocumentStatus;
import com.example.faculty.entity.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {
    private Long id;
    private Long facultyId;
    private String facultyName;
    private Long educationId;
    private Long experienceId;
    private DocumentType documentType;
    private String fileName;
    private String contentType;
    private long fileSize;
    private DocumentStatus status;
    private LocalDateTime uploadedAt;
    private LocalDateTime verifiedAt;
    private String verifiedByName;
    private String rejectionReason;
=======
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
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
