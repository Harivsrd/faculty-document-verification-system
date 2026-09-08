package com.example.faculty.controller;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/api/faculty/education/{id}/certificate")
    public DocumentResponse uploadEducationCertificate(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return documentService.uploadEducationCertificate(currentUser, id, file);
    }

    @PostMapping("/api/faculty/experience/{id}/certificate")
    public DocumentResponse uploadExperienceCertificate(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return documentService.uploadExperienceCertificate(currentUser, id, file);
    }

    @GetMapping("/api/faculty/documents")
    public List<DocumentResponse> getMyDocuments(@AuthenticationPrincipal User currentUser) {
        return documentService.getMyDocuments(currentUser);
    }
}
