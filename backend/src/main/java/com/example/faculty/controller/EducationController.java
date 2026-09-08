package com.example.faculty.controller;

<<<<<<< HEAD
import com.example.faculty.dto.EducationRequest;
import com.example.faculty.dto.EducationResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.EducationService;
=======
import com.example.faculty.dto.DocumentResponse;
import com.example.faculty.dto.EducationRequest;
import com.example.faculty.dto.EducationResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.DocumentService;
import com.example.faculty.service.EducationService;
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
@RequestMapping("/api/faculty/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;
<<<<<<< HEAD

    @GetMapping
    public List<EducationResponse> getMyEducation(@AuthenticationPrincipal User currentUser) {
        return educationService.getMyEducation(currentUser);
    }

    @PostMapping
    public ResponseEntity<EducationResponse> addEducation(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody EducationRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(educationService.addEducation(currentUser, request));
    }

    @PutMapping("/{id}")
    public EducationResponse updateEducation(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long id,
            @Valid @RequestBody EducationRequest request
    ) {
        return educationService.updateEducation(currentUser, id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducation(@AuthenticationPrincipal User currentUser, @PathVariable Long id) {
        educationService.deleteEducation(currentUser, id);
        return ResponseEntity.noContent().build();
    }
=======
    private final DocumentService documentService;
    private final CurrentUserProvider currentUserProvider;

    @GetMapping
    public List<EducationResponse> list() {
        return educationService.listMine(currentUserProvider.getCurrentUser());
    }

    @PostMapping
    public ResponseEntity<EducationResponse> create(@Valid @RequestBody EducationRequest request) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED).body(educationService.create(user, request));
    }

    @PutMapping("/{id}")
    public EducationResponse update(@PathVariable Long id, @Valid @RequestBody EducationRequest request) {
        return educationService.update(currentUserProvider.getCurrentUser(), id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        educationService.delete(currentUserProvider.getCurrentUser(), id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/certificate", consumes = "multipart/form-data")
    public ResponseEntity<DocumentResponse> uploadCertificate(@PathVariable Long id,
                                                                @RequestParam("file") MultipartFile file) {
        User user = currentUserProvider.getCurrentUser();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(documentService.uploadEducationCertificate(user, id, file));
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
