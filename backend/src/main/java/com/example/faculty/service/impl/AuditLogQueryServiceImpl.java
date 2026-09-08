package com.example.faculty.service.impl;

import com.example.faculty.dto.AuditLogResponse;
import com.example.faculty.entity.AuditAction;
import com.example.faculty.repository.AuditLogRepository;
import com.example.faculty.service.AuditLogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogQueryServiceImpl implements AuditLogQueryService {

    private final AuditLogRepository auditLogRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogResponse> filter(Long userId, String action, Pageable pageable) {
        AuditAction parsedAction = null;
        if (action != null && !action.isBlank()) {
            try {
                parsedAction = AuditAction.valueOf(action.trim().toUpperCase());
            } catch (IllegalArgumentException ignored) {
                // Unknown action filter -> return an empty page rather than erroring.
                return Page.empty(pageable);
            }
        }

        return auditLogRepository.filter(userId, parsedAction, pageable)
                .map(log -> AuditLogResponse.builder()
                        .id(log.getId())
                        .userName(log.getUser() != null ? log.getUser().getName() : null)
                        .userEmail(log.getUser() != null ? log.getUser().getEmail() : null)
                        .action(log.getAction())
                        .documentId(log.getDocument() != null ? log.getDocument().getId() : null)
                        .timestamp(log.getTimestamp())
                        .description(log.getDescription())
                        .build());
    }
}
