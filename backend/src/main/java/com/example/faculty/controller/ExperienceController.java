package com.example.faculty.controller;

import com.example.faculty.dto.ExperienceRequest;
import com.example.faculty.dto.ExperienceResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.ExperienceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/faculty/experience")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;

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
}
