package com.example.faculty.service;

import com.example.faculty.dto.ExperienceRequest;
import com.example.faculty.dto.ExperienceResponse;
import com.example.faculty.entity.User;

import java.util.List;

public interface ExperienceService {
    List<ExperienceResponse> getMyExperience(User currentUser);
    ExperienceResponse addExperience(User currentUser, ExperienceRequest request);
    ExperienceResponse updateExperience(User currentUser, Long experienceId, ExperienceRequest request);
    void deleteExperience(User currentUser, Long experienceId);
}
