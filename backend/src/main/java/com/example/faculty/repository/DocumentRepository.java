package com.example.faculty.repository;

import com.example.faculty.entity.Document;
import com.example.faculty.entity.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
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
=======

import java.util.List;
import java.util.Optional;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByFaculty_Id(Long facultyProfileId);
    List<Document> findByFaculty_IdAndStatus(Long facultyProfileId, DocumentStatus status);
    Optional<Document> findByIdAndFaculty_Id(Long id, Long facultyProfileId);
    Page<Document> findByStatus(DocumentStatus status, Pageable pageable);
    long countByStatus(DocumentStatus status);
    long countByFaculty_Id(Long facultyProfileId);
}
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
