package com.example.faculty.dto;

import com.example.faculty.entity.DocumentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationHistoryResponse {
    private Long id;
    private Long documentId;
    private String adminName;
    private DocumentStatus previousStatus;
    private DocumentStatus newStatus;
    private String comment;
    private LocalDateTime actionTimestamp;
}
