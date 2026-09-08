package com.example.faculty.service.impl;

import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.AuditLog;
import com.example.faculty.entity.Document;
import com.example.faculty.entity.User;
import com.example.faculty.repository.AuditLogRepository;
import com.example.faculty.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    @Override
    public void log(User user, AuditAction action, Document document, String description) {
        AuditLog entry = AuditLog.builder()
                .user(user)
                .action(action)
                .document(document)
                .description(description)
                .build();
        auditLogRepository.save(entry);
    }
}
