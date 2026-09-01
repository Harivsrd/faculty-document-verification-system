package com.example.faculty.service;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.entity.*;
import com.example.faculty.exception.ForbiddenException;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.repository.DocumentRepository;
import com.example.faculty.repository.VerificationHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final VerificationHistoryRepository verificationHistoryRepository;
    private final FileStorageService fileStorageService;
    private final FacultyProfileService facultyProfileService;
    private final EducationService educationService;
    private final ExperienceService experienceService;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public List<DocumentResponse> listMine(User user) {
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);
        return documentRepository.findByFaculty_Id(profile.getId())
                .stream().map(DocumentResponse::from).toList();
    }

    @Transactional
    public DocumentResponse uploadEducationCertificate(User user, Long educationId, MultipartFile file) {
        Education education = educationService.getOwned(user, educationId);
        FileStorageService.StoredFile stored = fileStorageService.store(file, education.getFaculty().getId(), "education");

        Document document = buildDocument(education.getFaculty(), DocumentType.EDUCATION_CERTIFICATE, stored);
        document.setEducation(education);

        Document saved = documentRepository.save(document);
        auditService.log(user, AuditAction.UPLOAD_DOCUMENT, saved, "Uploaded education certificate: " + stored.originalFileName());
        return DocumentResponse.from(saved);
    }

    @Transactional
    public DocumentResponse uploadExperienceCertificate(User user, Long experienceId, MultipartFile file) {
        Experience experience = experienceService.getOwned(user, experienceId);
        FileStorageService.StoredFile stored = fileStorageService.store(file, experience.getFaculty().getId(), "experience");

        Document document = buildDocument(experience.getFaculty(), DocumentType.EXPERIENCE_CERTIFICATE, stored);
        document.setExperience(experience);

        Document saved = documentRepository.save(document);
        auditService.log(user, AuditAction.UPLOAD_DOCUMENT, saved, "Uploaded experience certificate: " + stored.originalFileName());
        return DocumentResponse.from(saved);
    }

    /** Replaces a rejected document's file, resetting it to PENDING and clearing the rejection reason. */
    @Transactional
    public DocumentResponse reupload(User user, Long documentId, MultipartFile file) {
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);
        Document document = documentRepository.findByIdAndFaculty_Id(documentId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        if (document.getStatus() != DocumentStatus.REJECTED) {
            throw new ForbiddenException("Only rejected documents can be re-uploaded");
        }

        String category = document.getDocumentType() == DocumentType.EDUCATION_CERTIFICATE ? "education" : "experience";
        String oldPath = document.getFilePath();

        FileStorageService.StoredFile stored = fileStorageService.store(file, profile.getId(), category);

        DocumentStatus previousStatus = document.getStatus();

        document.setFileName(stored.originalFileName());
        document.setStoredFileName(stored.storedFileName());
        document.setFilePath(stored.relativePath());
        document.setContentType(stored.contentType());
        document.setFileSize(stored.size());
        document.setStatus(DocumentStatus.PENDING);
        document.setRejectionReason(null);
        document.setVerifiedAt(null);
        document.setVerifiedBy(null);

        Document saved = documentRepository.save(document);

        VerificationHistory history = VerificationHistory.builder()
                .document(saved)
                .admin(null)
                .previousStatus(previousStatus)
                .newStatus(DocumentStatus.PENDING)
                .comment("Faculty re-uploaded replacement document")
                .build();
        verificationHistoryRepository.save(history);

        fileStorageService.delete(oldPath);

        auditService.log(user, AuditAction.REUPLOAD_DOCUMENT, saved, "Re-uploaded document #" + documentId);
        return DocumentResponse.from(saved);
    }

    @Transactional
    public void delete(User user, Long documentId) {
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);
        Document document = documentRepository.findByIdAndFaculty_Id(documentId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Document not found"));

        fileStorageService.delete(document.getFilePath());
        documentRepository.delete(document);
        auditService.log(user, AuditAction.DELETE_DOCUMENT, "Deleted document #" + documentId);
    }

    private Document buildDocument(FacultyProfile faculty, DocumentType type, FileStorageService.StoredFile stored) {
        return Document.builder()
                .faculty(faculty)
                .documentType(type)
                .fileName(stored.originalFileName())
                .storedFileName(stored.storedFileName())
                .filePath(stored.relativePath())
                .contentType(stored.contentType())
                .fileSize(stored.size())
                .status(DocumentStatus.PENDING)
                .build();
    }
}
