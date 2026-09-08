package com.example.faculty.dto;

import com.example.faculty.entity.AuditAction;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponse {
    private Long id;
    private String userName;
    private String userEmail;
    private AuditAction action;
    private Long documentId;
    private LocalDateTime timestamp;
    private String description;
=======
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
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
