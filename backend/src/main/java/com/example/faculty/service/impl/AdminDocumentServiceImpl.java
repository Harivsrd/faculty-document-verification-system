package com.example.faculty.service.impl;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.dto.VerificationHistoryResponse;
import com.example.faculty.entity.*;
import com.example.faculty.exception.DocumentVerificationException;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.mapper.DocumentMapper;
import com.example.faculty.repository.DocumentRepository;
import com.example.faculty.repository.VerificationHistoryRepository;
import com.example.faculty.service.AdminDocumentService;
import com.example.faculty.service.AuditLogService;
import com.example.faculty.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminDocumentServiceImpl implements AdminDocumentService {

    private final DocumentRepository documentRepository;
    private final VerificationHistoryRepository verificationHistoryRepository;
    private final FileStorageService fileStorageService;
    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public Page<DocumentResponse> getPendingDocuments(Pageable pageable) {
        return documentRepository
                .findByStatusWithDetails(DocumentStatus.PENDING, pageable)
                .map(DocumentMapper::toResponse);
    }

    @Override
    @Transactional
    public DocumentResponse approve(User admin, Long documentId) {
        Document document = getDocumentEntity(documentId);
        DocumentStatus previousStatus = document.getStatus();

        document.setStatus(DocumentStatus.APPROVED);
        document.setVerifiedAt(LocalDateTime.now());
        document.setVerifiedBy(admin);
        document.setRejectionReason(null);
        document = documentRepository.save(document);

        recordHistory(document, admin, previousStatus, DocumentStatus.APPROVED, "Approved by admin");
        auditLogService.log(admin, AuditAction.APPROVE_DOCUMENT, document,
                "Approved document " + document.getFileName());

        return DocumentMapper.toResponse(document);
    }

    @Override
    @Transactional
    public DocumentResponse reject(User admin, Long documentId, String reason) {
        if (reason == null || reason.isBlank()) {
            throw new DocumentVerificationException("A rejection reason is required");
        }

        Document document = getDocumentEntity(documentId);
        DocumentStatus previousStatus = document.getStatus();

        document.setStatus(DocumentStatus.REJECTED);
        document.setVerifiedAt(LocalDateTime.now());
        document.setVerifiedBy(admin);
        document.setRejectionReason(reason);
        document = documentRepository.save(document);

        recordHistory(document, admin, previousStatus, DocumentStatus.REJECTED, reason);
        auditLogService.log(admin, AuditAction.REJECT_DOCUMENT, document,
                "Rejected document " + document.getFileName() + ": " + reason);

        return DocumentMapper.toResponse(document);
    }

    @Override
    public List<VerificationHistoryResponse> getHistory(Long documentId) {
        return verificationHistoryRepository.findByDocumentIdOrderByActionTimestampDesc(documentId)
                .stream()
                .map(h -> VerificationHistoryResponse.builder()
                        .id(h.getId())
                        .documentId(h.getDocument().getId())
                        .adminName(h.getAdmin().getName())
                        .previousStatus(h.getPreviousStatus())
                        .newStatus(h.getNewStatus())
                        .comment(h.getComment())
                        .actionTimestamp(h.getActionTimestamp())
                        .build())
                .toList();
    }

    @Override
    public Document getDocumentEntity(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    @Override
    public Resource loadAsResource(Document document) {
        Path filePath = fileStorageService.resolve(document.getFilePath());
        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Physical file not found for this document");
            }
            return resource;
        } catch (MalformedURLException e) {
            throw new ResourceNotFoundException("Could not read file for this document");
        }
    }

    @Override
    public void recordPreview(User admin, Document document) {
        auditLogService.log(admin, AuditAction.PREVIEW_DOCUMENT, document,
                "Previewed document " + document.getFileName());
    }

    @Override
    public void recordDownload(User admin, Document document) {
        auditLogService.log(admin, AuditAction.DOWNLOAD_DOCUMENT, document,
                "Downloaded document " + document.getFileName());
    }

    private void recordHistory(Document document, User admin, DocumentStatus previous,
                                DocumentStatus updated, String comment) {
        VerificationHistory history = VerificationHistory.builder()
                .document(document)
                .admin(admin)
                .previousStatus(previous)
                .newStatus(updated)
                .comment(comment)
                .build();
        verificationHistoryRepository.save(history);
    }
}
