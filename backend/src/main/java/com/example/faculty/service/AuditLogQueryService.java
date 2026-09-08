package com.example.faculty.service;

import com.example.faculty.dto.AuditLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogQueryService {
    Page<AuditLogResponse> filter(Long userId, String action, Pageable pageable);
}
