package com.example.faculty.service;

import com.example.faculty.dto.FacultyProfileRequest;
import com.example.faculty.dto.FacultyProfileResponse;
import com.example.faculty.entity.User;

public interface FacultyProfileService {
    FacultyProfileResponse getMyProfile(User currentUser);
    FacultyProfileResponse updateMyProfile(User currentUser, FacultyProfileRequest request);
}
