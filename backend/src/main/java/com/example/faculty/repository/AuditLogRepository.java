package com.example.faculty.repository;

import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("""
        SELECT a
        FROM AuditLog a
        LEFT JOIN FETCH a.user
        LEFT JOIN FETCH a.document
        WHERE (:userId IS NULL OR a.user.id = :userId)
          AND (:action IS NULL OR a.action = :action)
        ORDER BY a.timestamp DESC
    """)
    Page<AuditLog> filter(
            @Param("userId") Long userId,
            @Param("action") AuditAction action,
            Pageable pageable
    );
}