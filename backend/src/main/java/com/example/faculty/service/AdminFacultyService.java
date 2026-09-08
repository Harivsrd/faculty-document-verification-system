package com.example.faculty.service;

import com.example.faculty.dto.FacultyDetailResponse;
import com.example.faculty.dto.FacultySummaryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminFacultyService {
    Page<FacultySummaryResponse> listFaculty(String keyword, Pageable pageable);
    FacultyDetailResponse getFacultyDetail(Long facultyId);
}
