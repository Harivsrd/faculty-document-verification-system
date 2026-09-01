package com.example.faculty.controller;

import com.example.faculty.dto.EducationResponse;
import com.example.faculty.dto.ExperienceResponse;
import com.example.faculty.dto.PageResponse;
import com.example.faculty.dto.PublicFacultyResponse;
import com.example.faculty.service.PublicDirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/faculty")
@RequiredArgsConstructor
public class PublicFacultyController {

    private final PublicDirectoryService publicDirectoryService;

    @GetMapping
    public PageResponse<PublicFacultyResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());
        return publicDirectoryService.listApprovedFaculty(pageable);
    }

    @GetMapping("/{facultyId}")
    public PublicFacultyResponse getFaculty(@PathVariable Long facultyId) {
        return publicDirectoryService.getApprovedFaculty(facultyId);
    }

    @GetMapping("/{facultyId}/education")
    public List<EducationResponse> getVerifiedEducation(@PathVariable Long facultyId) {
        return publicDirectoryService.getVerifiedEducation(facultyId);
    }

    @GetMapping("/{facultyId}/experience")
    public List<ExperienceResponse> getVerifiedExperience(@PathVariable Long facultyId) {
        return publicDirectoryService.getVerifiedExperience(facultyId);
    }
}
