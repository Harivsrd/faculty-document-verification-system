package com.example.faculty.controller;

import com.example.faculty.dto.FacultyDetailResponse;
import com.example.faculty.dto.FacultySummaryResponse;
import com.example.faculty.service.AdminFacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/faculty")
@RequiredArgsConstructor
public class AdminFacultyController {

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
    }
}
