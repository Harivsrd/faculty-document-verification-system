package com.example.faculty.controller;

import com.example.faculty.dto.PublicFacultyDetailResponse;
import com.example.faculty.dto.PublicFacultyResponse;
import com.example.faculty.service.PublicFacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/faculty")
@RequiredArgsConstructor
public class PublicFacultyController {

    private final PublicFacultyService publicFacultyService;

    @GetMapping
    public List<PublicFacultyResponse> search(@RequestParam(required = false) String keyword) {
        return publicFacultyService.searchApprovedFaculty(keyword);
    }

    @GetMapping("/{facultyId}")
    public PublicFacultyDetailResponse getDetail(@PathVariable Long facultyId) {
        return publicFacultyService.getApprovedFacultyDetail(facultyId);
    }
}
