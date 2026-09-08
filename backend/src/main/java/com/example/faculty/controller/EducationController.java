package com.example.faculty.controller;

import com.example.faculty.dto.EducationRequest;
import com.example.faculty.dto.EducationResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.EducationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty/education")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

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
}
