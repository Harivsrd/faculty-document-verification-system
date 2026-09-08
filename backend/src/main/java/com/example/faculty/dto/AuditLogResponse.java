package com.example.faculty.dto;

import com.example.faculty.entity.AuditAction;
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
}
