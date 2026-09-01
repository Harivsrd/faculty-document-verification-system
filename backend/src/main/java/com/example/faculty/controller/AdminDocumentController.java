package com.example.faculty.controller;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.dto.DocumentVerificationRequest;
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
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/admin/documents")
@RequiredArgsConstructor
public class AdminDocumentController {

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
}
