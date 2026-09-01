package com.example.faculty.controller;

import com.example.faculty.dto.*;
import com.example.faculty.service.AdminService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/admin/faculty")
@RequiredArgsConstructor
public class AdminFacultyController {

    private final AdminService adminService;

    @GetMapping
    public PageResponse<FacultyProfileResponse> listFaculty(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return adminService.listFaculty(search, pageable);
    }

    @GetMapping("/{facultyId}")
    public FacultyProfileResponse getFaculty(@PathVariable Long facultyId) {
        return adminService.getFacultyProfile(facultyId);
    }

    @GetMapping("/{facultyId}/education")
    public List<EducationResponse> getEducation(@PathVariable Long facultyId) {
        return adminService.getFacultyEducation(facultyId);
    }

    @GetMapping("/{facultyId}/experience")
    public List<ExperienceResponse> getExperience(@PathVariable Long facultyId) {
        return adminService.getFacultyExperience(facultyId);
    }

    @GetMapping("/{facultyId}/documents")
    public List<DocumentResponse> getDocuments(@PathVariable Long facultyId) {
        return adminService.getFacultyDocuments(facultyId);
    }
}
