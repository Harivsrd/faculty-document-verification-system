package com.example.faculty.controller;

import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.DocumentService;
import com.example.faculty.util.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/faculty/documents")
@RequiredArgsConstructor
public class FacultyDocumentController {

    private final DocumentService documentService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping
    public List<DocumentResponse> list() {
        return documentService.listMine(currentUserProvider.getCurrentUser());
    }

    @PostMapping(value = "/{id}/reupload", consumes = "multipart/form-data")
    public DocumentResponse reupload(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        User user = currentUserProvider.getCurrentUser();
        return documentService.reupload(user, id, file);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        documentService.delete(currentUserProvider.getCurrentUser(), id);
        return ResponseEntity.noContent().build();
    }
}
