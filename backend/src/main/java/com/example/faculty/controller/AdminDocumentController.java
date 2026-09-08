package com.example.faculty.controller;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.dto.DocumentVerificationRequest;
<<<<<<< HEAD
import com.example.faculty.dto.VerificationHistoryResponse;
import com.example.faculty.entity.Document;
import com.example.faculty.entity.User;
import com.example.faculty.service.AdminDocumentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
=======
import com.example.faculty.dto.PageResponse;
import com.example.faculty.dto.VerificationHistoryResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.AdminService;
import com.example.faculty.util.CurrentUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/admin/documents")
@RequiredArgsConstructor
public class AdminDocumentController {

<<<<<<< HEAD
    private final AdminDocumentService adminDocumentService;

    @GetMapping("/pending")
    public Page<DocumentResponse> getPendingDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").ascending());
        return adminDocumentService.getPendingDocuments(pageable);
    }

    @PutMapping("/{documentId}/approve")
    public DocumentResponse approve(@AuthenticationPrincipal User admin, @PathVariable Long documentId) {
        return adminDocumentService.approve(admin, documentId);
    }

    @PutMapping("/{documentId}/reject")
    public DocumentResponse reject(
            @AuthenticationPrincipal User admin,
            @PathVariable Long documentId,
            @Valid @RequestBody DocumentVerificationRequest request
    ) {
        return adminDocumentService.reject(admin, documentId, request.getReason());
    }

    @GetMapping("/{documentId}/history")
    public List<VerificationHistoryResponse> getHistory(@PathVariable Long documentId) {
        return adminDocumentService.getHistory(documentId);
    }

    @GetMapping("/{documentId}/preview")
    public ResponseEntity<Resource> preview(@AuthenticationPrincipal User admin, @PathVariable Long documentId) {
        Document document = adminDocumentService.getDocumentEntity(documentId);
        Resource resource = adminDocumentService.loadAsResource(document);
        adminDocumentService.recordPreview(admin, document);

        MediaType mediaType = resolveMediaType(document.getContentType());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + sanitize(document.getFileName()) + "\"")
                .body(resource);
    }

    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> download(@AuthenticationPrincipal User admin, @PathVariable Long documentId) {
        Document document = adminDocumentService.getDocumentEntity(documentId);
        Resource resource = adminDocumentService.loadAsResource(document);
        adminDocumentService.recordDownload(admin, document);

        MediaType mediaType = resolveMediaType(document.getContentType());

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + sanitize(document.getFileName()) + "\"")
                .body(resource);
    }

    private MediaType resolveMediaType(String contentType) {
        if (contentType == null) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
        try {
            return MediaType.parseMediaType(contentType);
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private String sanitize(String fileName) {
        if (fileName == null) {
            return "document";
        }
        return new String(fileName.replaceAll("[\\r\\n\"]", "_").getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }
=======
    private final AdminService adminService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping("/pending")
    public PageResponse<DocumentResponse> pending(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("uploadedAt").ascending());
        return adminService.getPendingDocuments(pageable);
    }

    @PutMapping("/{documentId}/approve")
    public DocumentResponse approve(@PathVariable Long documentId) {
        User admin = currentUserProvider.getCurrentUser();
        return adminService.approve(admin, documentId);
    }

    @PutMapping("/{documentId}/reject")
    public DocumentResponse reject(@PathVariable Long documentId,
                                    @Valid @RequestBody DocumentVerificationRequest request) {
        User admin = currentUserProvider.getCurrentUser();
        return adminService.reject(admin, documentId, request.reason());
    }

    @GetMapping("/{documentId}/history")
    public List<VerificationHistoryResponse> history(@PathVariable Long documentId) {
        return adminService.getHistory(documentId);
    }

    /** Streams the file inline for in-browser preview. Never exposes the physical filesystem path. */
    @GetMapping("/{documentId}/preview")
    public ResponseEntity<Resource> preview(@PathVariable Long documentId) {
        User admin = currentUserProvider.getCurrentUser();
        AdminService.DocumentFile file = adminService.loadDocumentFile(admin, documentId, true);

        Resource resource = new FileSystemResource(file.path());
        ContentDisposition disposition = ContentDisposition.inline()
                .filename(file.fileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(resource);
    }

    /** Forces a file download as an attachment. Requires ADMIN role, enforced at the SecurityConfig level. */
    @GetMapping("/{documentId}/download")
    public ResponseEntity<Resource> download(@PathVariable Long documentId) {
        User admin = currentUserProvider.getCurrentUser();
        AdminService.DocumentFile file = adminService.loadDocumentFile(admin, documentId, false);

        Resource resource = new FileSystemResource(file.path());
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(file.fileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(resource);
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
