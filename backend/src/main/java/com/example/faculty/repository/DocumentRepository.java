package com.example.faculty.repository;

import com.example.faculty.entity.Document;
import com.example.faculty.entity.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    List<Document> findByFacultyId(Long facultyId);

    List<Document> findByFacultyUserId(Long userId);

    @Query("""
        SELECT d
        FROM Document d
        LEFT JOIN FETCH d.faculty
        LEFT JOIN FETCH d.education
        LEFT JOIN FETCH d.experience
        LEFT JOIN FETCH d.verifiedBy
        WHERE d.status = :status
        ORDER BY d.uploadedAt DESC
    """)
    Page<Document> findByStatusWithDetails(
            @Param("status") DocumentStatus status,
            Pageable pageable
    );

    Page<Document> findByStatus(DocumentStatus status, Pageable pageable);

    long countByStatus(DocumentStatus status);

    long countByFacultyId(Long facultyId);
}