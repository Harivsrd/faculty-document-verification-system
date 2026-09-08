package com.example.faculty.repository;

import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

<<<<<<< HEAD
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
=======
import java.time.LocalDateTime;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    @Query("""
           SELECT a FROM AuditLog a
           WHERE (:action IS NULL OR a.action = :action)
           AND (:userId IS NULL OR a.user.id = :userId)
           AND (:from IS NULL OR a.timestamp >= :from)
           AND (:to IS NULL OR a.timestamp <= :to)
           ORDER BY a.timestamp DESC
           """)
    Page<AuditLog> filter(@Param("action") AuditAction action,
                           @Param("userId") Long userId,
                           @Param("from") LocalDateTime from,
                           @Param("to") LocalDateTime to,
                           Pageable pageable);
}
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
