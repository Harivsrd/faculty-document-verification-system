package com.example.faculty.dto;

import com.example.faculty.entity.DocumentStatus;
import com.example.faculty.entity.VerificationHistory;

import java.time.LocalDateTime;

public record VerificationHistoryResponse(
        Long id,
        Long documentId,
        String adminName,
        DocumentStatus previousStatus,
        DocumentStatus newStatus,
        String comment,
        LocalDateTime actionTimestamp
) {
    public static VerificationHistoryResponse from(VerificationHistory h) {
        return new VerificationHistoryResponse(
                h.getId(),
                h.getDocument().getId(),
                h.getAdmin() != null ? h.getAdmin().getName() : "System",
                h.getPreviousStatus(),
                h.getNewStatus(),
                h.getComment(),
                h.getActionTimestamp()
        );
    }
}
