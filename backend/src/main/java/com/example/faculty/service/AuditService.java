package com.example.faculty.service;

import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.AuditLog;
import com.example.faculty.entity.Document;
import com.example.faculty.entity.User;
import com.example.faculty.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(User user, AuditAction action, Document document, String description) {
        AuditLog entry = AuditLog.builder()
                .user(user)
                .action(action)
                .document(document)
                .description(description)
                .build();
        auditLogRepository.save(entry);
    }

    public void log(User user, AuditAction action, String description) {
        log(user, action, null, description);
    }
}
