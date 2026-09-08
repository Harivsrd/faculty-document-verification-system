package com.example.faculty.service;

import com.example.faculty.dto.PublicFacultyDetailResponse;
import com.example.faculty.dto.PublicFacultyResponse;

import java.util.List;

public interface PublicFacultyService {
    List<PublicFacultyResponse> searchApprovedFaculty(String keyword);
    PublicFacultyDetailResponse getApprovedFacultyDetail(Long facultyId);
}
