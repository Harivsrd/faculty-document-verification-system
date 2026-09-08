package com.example.faculty.dto;

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
}
