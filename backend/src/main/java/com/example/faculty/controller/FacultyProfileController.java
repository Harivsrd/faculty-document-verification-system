package com.example.faculty.controller;

import com.example.faculty.dto.FacultyProfileRequest;
import com.example.faculty.dto.FacultyProfileResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.FacultyProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty/profile")
@RequiredArgsConstructor
public class FacultyProfileController {

    private final FacultyProfileService facultyProfileService;

    @GetMapping
    public FacultyProfileResponse getProfile(@AuthenticationPrincipal User currentUser) {
        return facultyProfileService.getMyProfile(currentUser);
    }

    @PutMapping
    public FacultyProfileResponse updateProfile(
            @AuthenticationPrincipal User currentUser,
            @Valid @RequestBody FacultyProfileRequest request
    ) {
        return facultyProfileService.updateMyProfile(currentUser, request);
    }
}
