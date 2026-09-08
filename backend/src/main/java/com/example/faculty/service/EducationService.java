package com.example.faculty.service;

import com.example.faculty.dto.EducationRequest;
import com.example.faculty.dto.EducationResponse;
import com.example.faculty.entity.User;

import java.util.List;

public interface EducationService {
    List<EducationResponse> getMyEducation(User currentUser);
    EducationResponse addEducation(User currentUser, EducationRequest request);
    EducationResponse updateEducation(User currentUser, Long educationId, EducationRequest request);
    void deleteEducation(User currentUser, Long educationId);
}
