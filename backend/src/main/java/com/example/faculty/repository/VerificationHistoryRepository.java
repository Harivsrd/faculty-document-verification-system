package com.example.faculty.repository;

import com.example.faculty.entity.VerificationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VerificationHistoryRepository extends JpaRepository<VerificationHistory, Long> {
<<<<<<< HEAD
    List<VerificationHistory> findByDocumentIdOrderByActionTimestampDesc(Long documentId);
=======
    List<VerificationHistory> findByDocument_IdOrderByActionTimestampDesc(Long documentId);
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
