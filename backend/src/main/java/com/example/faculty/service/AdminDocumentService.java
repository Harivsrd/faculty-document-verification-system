package com.example.faculty.service;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.dto.VerificationHistoryResponse;
import com.example.faculty.entity.Document;
import com.example.faculty.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminDocumentService {

    Page<DocumentResponse> getPendingDocuments(Pageable pageable);

    DocumentResponse approve(User admin, Long documentId);

    DocumentResponse reject(User admin, Long documentId, String reason);

    List<VerificationHistoryResponse> getHistory(Long documentId);

    /** Returns the entity so the controller can stream the underlying file. */
    Document getDocumentEntity(Long documentId);

    Resource loadAsResource(Document document);

    void recordPreview(User admin, Document document);

    void recordDownload(User admin, Document document);
}
