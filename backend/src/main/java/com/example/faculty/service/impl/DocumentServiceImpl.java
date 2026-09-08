package com.example.faculty.service.impl;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.entity.*;
import com.example.faculty.exception.ForbiddenException;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.mapper.DocumentMapper;
import com.example.faculty.repository.DocumentRepository;
import com.example.faculty.repository.EducationRepository;
import com.example.faculty.repository.ExperienceRepository;
import com.example.faculty.service.AuditLogService;
import com.example.faculty.service.DocumentService;
import com.example.faculty.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final FileStorageService fileStorageService;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public DocumentResponse uploadEducationCertificate(User currentUser, Long educationId, MultipartFile file) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education record not found"));

        assertOwnership(currentUser, education.getFaculty());

        Optional<Document> existing = education.getDocuments().stream()
                .filter(d -> d.getDocumentType() == DocumentType.EDUCATION_CERTIFICATE)
                .findFirst();

        Document document = storeAndUpsert(currentUser, education.getFaculty(), education, null,
                DocumentType.EDUCATION_CERTIFICATE, "education", file, existing);

        return DocumentMapper.toResponse(document);
    }

    @Override
    @Transactional
    public DocumentResponse uploadExperienceCertificate(User currentUser, Long experienceId, MultipartFile file) {
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience record not found"));

        assertOwnership(currentUser, experience.getFaculty());

        Optional<Document> existing = experience.getDocuments().stream()
                .filter(d -> d.getDocumentType() == DocumentType.EXPERIENCE_CERTIFICATE)
                .findFirst();

        Document document = storeAndUpsert(currentUser, experience.getFaculty(), null, experience,
                DocumentType.EXPERIENCE_CERTIFICATE, "experience", file, existing);

        return DocumentMapper.toResponse(document);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentResponse> getMyDocuments(User currentUser) {
        return documentRepository.findByFacultyUserId(currentUser.getId())
                .stream()
                .map(DocumentMapper::toResponse)
                .toList();
    }

    private Document storeAndUpsert(User currentUser, FacultyProfile faculty, Education education,
                                     Experience experience, DocumentType type, String category,
                                     MultipartFile file, Optional<Document> existing) {

        FileStorageService.StoredFile stored = fileStorageService.store(file, faculty.getId(), category);

        boolean isReupload = existing.isPresent();
        Document document = existing.orElseGet(() -> Document.builder()
                .faculty(faculty)
                .education(education)
                .experience(experience)
                .documentType(type)
                .build());

        // Remove the previous physical file on reupload to avoid orphaned files.
        if (isReupload && document.getFilePath() != null) {
            fileStorageService.delete(document.getFilePath());
        }

        document.setFileName(file.getOriginalFilename());
        document.setStoredFileName(stored.storedFileName());
        document.setFilePath(stored.relativeFilePath());
        document.setContentType(file.getContentType());
        document.setFileSize(file.getSize());
        document.setStatus(DocumentStatus.PENDING);
        document.setVerifiedAt(null);
        document.setVerifiedBy(null);
        document.setRejectionReason(null);

        document = documentRepository.save(document);

        auditLogService.log(currentUser,
                isReupload ? AuditAction.REUPLOAD_DOCUMENT : AuditAction.UPLOAD_DOCUMENT,
                document,
                (isReupload ? "Re-uploaded " : "Uploaded ") + type + " (" + file.getOriginalFilename() + ")");

        return document;
    }

    private void assertOwnership(User currentUser, FacultyProfile faculty) {
        if (!faculty.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You do not have access to this record");
        }
    }
}
