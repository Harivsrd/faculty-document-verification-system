package com.example.faculty.controller;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.dto.DocumentVerificationRequest;
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
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/admin/documents")
@RequiredArgsConstructor
public class AdminDocumentController {

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
}
