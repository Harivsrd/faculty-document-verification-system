package com.example.faculty.controller;

<<<<<<< HEAD
import com.example.faculty.dto.FacultyDetailResponse;
import com.example.faculty.dto.FacultySummaryResponse;
import com.example.faculty.service.AdminFacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
=======
import com.example.faculty.dto.*;
import com.example.faculty.service.AdminService;
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
<<<<<<< HEAD
=======
import lombok.RequiredArgsConstructor;

import java.util.List;
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3

@RestController
@RequestMapping("/api/admin/faculty")
@RequiredArgsConstructor
public class AdminFacultyController {

<<<<<<< HEAD
    private final AdminFacultyService adminFacultyService;

    @GetMapping
    public Page<FacultySummaryResponse> listFaculty(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fullName").ascending());
        return adminFacultyService.listFaculty(keyword, pageable);
    }

    @GetMapping("/{facultyId}")
    public FacultyDetailResponse getFacultyDetail(@PathVariable Long facultyId) {
        return adminFacultyService.getFacultyDetail(facultyId);
=======
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
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
    }
}
