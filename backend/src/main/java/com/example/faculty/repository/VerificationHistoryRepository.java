package com.example.faculty.repository;

import com.example.faculty.entity.VerificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationHistoryRepository extends JpaRepository<VerificationHistory, Long> {
    List<VerificationHistory> findByDocumentIdOrderByActionTimestampDesc(Long documentId);
}
