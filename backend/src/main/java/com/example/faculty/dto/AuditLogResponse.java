package com.example.faculty.dto;

import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.AuditLog;

import java.time.LocalDateTime;

public record AuditLogResponse(
        Long id,
        Long userId,
        String userName,
        AuditAction action,
        Long documentId,
        LocalDateTime timestamp,
        String description
) {
    public static AuditLogResponse from(AuditLog a) {
        return new AuditLogResponse(
                a.getId(),
                a.getUser() != null ? a.getUser().getId() : null,
                a.getUser() != null ? a.getUser().getName() : "Unknown",
                a.getAction(),
                a.getDocument() != null ? a.getDocument().getId() : null,
                a.getTimestamp(),
                a.getDescription()
        );
    }
}
