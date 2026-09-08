package com.example.faculty.controller;

<<<<<<< HEAD
import com.example.faculty.dto.ExperienceRequest;
import com.example.faculty.dto.ExperienceResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.ExperienceService;
=======
import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.dto.ExperienceRequest;
import com.example.faculty.dto.ExperienceResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.DocumentService;
import com.example.faculty.service.ExperienceService;
import com.example.faculty.util.CurrentUserProvider;
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
<<<<<<< HEAD
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
=======
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3

import java.util.List;

@RestController
@RequestMapping("/api/faculty/experience")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;
<<<<<<< HEAD

    @GetMapping
    public List<ExperienceResponse> getMyExperience(@AuthenticationPrincipal User currentUser) {
        return experienceService.getMyExperience(currentUser);
    }

    @PostMapping
    public ResponseEntity<ExperienceResponse> addExperience(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody ExperienceRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(experienceService.addExperience(currentUser, request));
    }

    @PutMapping("/{id}")
    public ExperienceResponse updateExperience(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @Valid @RequestBody ExperienceRequest request
    ) {
        return experienceService.updateExperience(currentUser, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperience(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {
        experienceService.deleteExperience(currentUser, id);
        return ResponseEntity.noContent().build();
    }
=======
    private final DocumentService documentService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping
    public List<ExperienceResponse> list() {
        return experienceService.listMine(currentUserProvider.getCurrentUser());
    }

    @PostMapping
    public ResponseEntity<ExperienceResponse> create(@Valid @RequestBody ExperienceRequest request) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(experienceService.create(user, request));
    }

    @PutMapping("/{id}")
    public ExperienceResponse update(@PathVariable Long id, @Valid @RequestBody ExperienceRequest request) {
        return experienceService.update(currentUserProvider.getCurrentUser(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        experienceService.delete(currentUserProvider.getCurrentUser(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/certificate", consumes = "multipart/form-data")
    public ResponseEntity<DocumentResponse> uploadCertificate(@PathVariable Long id,
                                                                @RequestParam("file") MultipartFile file) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.uploadExperienceCertificate(user, id, file));
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
