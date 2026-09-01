package com.example.faculty.repository;

import com.example.faculty.entity.Document;
import com.example.faculty.entity.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
