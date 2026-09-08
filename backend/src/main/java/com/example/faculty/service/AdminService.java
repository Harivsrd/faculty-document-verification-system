package com.example.faculty.service;

import com.example.faculty.dto.*;
import com.example.faculty.entity.*;
import com.example.faculty.exception.DocumentVerificationException;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final FacultyProfileRepository facultyProfileRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final DocumentRepository documentRepository;
    private final VerificationHistoryRepository verificationHistoryRepository;
    private final AuditLogRepository auditLogRepository;
    private final FileStorageService fileStorageService;
    private final AuditService auditService;

    // ---------- Faculty management ----------

    @Transactional(readOnly = true)
    public PageResponse<FacultyProfileResponse> listFaculty(String search, Pageable pageable) {
        Page<FacultyProfile> page = facultyProfileRepository.search(search, pageable);
        return PageResponse.from(page.map(FacultyProfileResponse::from));
    }

    @Transactional(readOnly = true)
    public FacultyProfileResponse getFacultyProfile(Long facultyId) {
        return FacultyProfileResponse.from(getFacultyOrThrow(facultyId));
    }

    @Transactional(readOnly = true)
    public List<EducationResponse> getFacultyEducation(Long facultyId) {
        getFacultyOrThrow(facultyId);
        return educationRepository.findByFaculty_Id(facultyId).stream().map(EducationResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getFacultyExperience(Long facultyId) {
        getFacultyOrThrow(facultyId);
        return experienceRepository.findByFaculty_Id(facultyId).stream().map(ExperienceResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> getFacultyDocuments(Long facultyId) {
        getFacultyOrThrow(facultyId);
        return documentRepository.findByFaculty_Id(facultyId).stream().map(DocumentResponse::from).toList();
    }

    private FacultyProfile getFacultyOrThrow(Long facultyId) {
        return facultyProfileRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found"));
    }

    // ---------- Document verification ----------

    @Transactional(readOnly = true)
    public PageResponse<DocumentResponse> getPendingDocuments(Pageable pageable) {
        Page<DocumentResponse> page = documentRepository.findByStatus(DocumentStatus.PENDING, pageable)
                .map(DocumentResponse::from);
        return PageResponse.from(page);
    }

    @Transactional
    public DocumentResponse approve(User admin, Long documentId) {
        Document document = getDocumentOrThrow(documentId);

        if (document.getStatus() == DocumentStatus.APPROVED) {
            throw new DocumentVerificationException("Document is already approved");
        }

        DocumentStatus previous = document.getStatus();
        document.setStatus(DocumentStatus.APPROVED);
        document.setVerifiedAt(LocalDateTime.now());
        document.setVerifiedBy(admin);
        document.setRejectionReason(null);

        Document saved = documentRepository.save(document);
        recordHistory(saved, admin, previous, DocumentStatus.APPROVED, "Approved by admin");
        auditService.log(admin, AuditAction.APPROVE_DOCUMENT, saved, "Approved document #" + documentId);

        return DocumentResponse.from(saved);
    }

    @Transactional
    public DocumentResponse reject(User admin, Long documentId, String reason) {
        Document document = getDocumentOrThrow(documentId);

        DocumentStatus previous = document.getStatus();
        document.setStatus(DocumentStatus.REJECTED);
        document.setVerifiedAt(LocalDateTime.now());
        document.setVerifiedBy(admin);
        document.setRejectionReason(reason);

        Document saved = documentRepository.save(document);
        recordHistory(saved, admin, previous, DocumentStatus.REJECTED, reason);
        auditService.log(admin, AuditAction.REJECT_DOCUMENT, saved, "Rejected document #" + documentId + ": " + reason);

        return DocumentResponse.from(saved);
    }

    private void recordHistory(Document document, User admin, DocumentStatus previous, DocumentStatus next, String comment) {
        VerificationHistory history = VerificationHistory.builder()
                .document(document)
                .admin(admin)
                .previousStatus(previous)
                .newStatus(next)
                .comment(comment)
                .build();
        verificationHistoryRepository.save(history);
    }

    @Transactional(readOnly = true)
    public List<VerificationHistoryResponse> getHistory(Long documentId) {
        getDocumentOrThrow(documentId);
        return verificationHistoryRepository.findByDocument_IdOrderByActionTimestampDesc(documentId)
                .stream().map(VerificationHistoryResponse::from).toList();
    }

    // ---------- Preview / download ----------

    @Transactional
    public DocumentFile loadDocumentFile(User admin, Long documentId, boolean isPreview) {
        Document document = getDocumentOrThrow(documentId);
        Path path = fileStorageService.resolve(document.getFilePath());

        auditService.log(admin, isPreview ? AuditAction.PREVIEW_DOCUMENT : AuditAction.DOWNLOAD_DOCUMENT,
                document, (isPreview ? "Previewed" : "Downloaded") + " document #" + documentId);

        return new DocumentFile(path, document.getFileName(), document.getContentType());
    }

    private Document getDocumentOrThrow(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));
    }

    // ---------- Dashboard ----------

    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboard() {
        return new AdminDashboardResponse(
                facultyProfileRepository.count(),
                documentRepository.count(),
                documentRepository.countByStatus(DocumentStatus.PENDING),
                documentRepository.countByStatus(DocumentStatus.APPROVED),
                documentRepository.countByStatus(DocumentStatus.REJECTED)
        );
    }

    // ---------- Audit logs ----------

    @Transactional(readOnly = true)
    public PageResponse<AuditLogResponse> getAuditLogs(AuditAction action, Long userId,
                                                         LocalDateTime from, LocalDateTime to, Pageable pageable) {
        Page<AuditLogResponse> page = auditLogRepository.filter(action, userId, from, to, pageable)
                .map(AuditLogResponse::from);
        return PageResponse.from(page);
    }

    public record DocumentFile(Path path, String fileName, String contentType) {}
}
