package com.example.faculty.service.impl;

import org.springframework.transaction.annotation.Transactional;
import com.example.faculty.dto.*;
import com.example.faculty.entity.Education;
import com.example.faculty.entity.Experience;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.mapper.DocumentMapper;
import com.example.faculty.repository.DocumentRepository;
import com.example.faculty.repository.EducationRepository;
import com.example.faculty.repository.ExperienceRepository;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.service.AdminFacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminFacultyServiceImpl implements AdminFacultyService {

    private final FacultyProfileRepository facultyProfileRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final DocumentRepository documentRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<FacultySummaryResponse> listFaculty(String keyword, Pageable pageable) {
        String normalizedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        Page<FacultyProfile> page = facultyProfileRepository.search(normalizedKeyword, pageable);

        return page.map(profile -> FacultySummaryResponse.builder()
                .facultyId(profile.getId())
                .fullName(profile.getFullName())
                .email(profile.getUser().getEmail())
                .department(profile.getDepartment())
                .specialization(profile.getSpecialization())
                .documentCount(documentRepository.countByFacultyId(profile.getId()))
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyDetailResponse getFacultyDetail(Long facultyId) {
        FacultyProfile profile = facultyProfileRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found"));

        var education = educationRepository.findByFacultyId(facultyId).stream()
                .map(this::toEducationResponse)
                .toList();

        var experience = experienceRepository.findByFacultyId(facultyId).stream()
                .map(this::toExperienceResponse)
                .toList();

        var documents = documentRepository.findByFacultyId(facultyId).stream()
                .map(DocumentMapper::toResponse)
                .toList();

        return FacultyDetailResponse.builder()
                .profile(FacultyProfileServiceImpl.toResponse(profile))
                .education(education)
                .experience(experience)
                .documents(documents)
                .build();
    }

    private EducationResponse toEducationResponse(Education education) {
        return EducationResponse.builder()
                .id(education.getId())
                .qualificationType(education.getQualificationType())
                .institution(education.getInstitution())
                .boardOrUniversity(education.getBoardOrUniversity())
                .courseName(education.getCourseName())
                .yearOfPassing(education.getYearOfPassing())
                .percentageOrCgpa(education.getPercentageOrCgpa())
                .documents(education.getDocuments().stream().map(DocumentMapper::toResponse).toList())
                .build();
    }

    private ExperienceResponse toExperienceResponse(Experience experience) {
        return ExperienceResponse.builder()
                .id(experience.getId())
                .organization(experience.getOrganization())
                .designation(experience.getDesignation())
                .department(experience.getDepartment())
                .startDate(experience.getStartDate())
                .endDate(experience.getEndDate())
                .currentlyWorking(experience.isCurrentlyWorking())
                .description(experience.getDescription())
                .documents(experience.getDocuments().stream().map(DocumentMapper::toResponse).toList())
                .build();
    }
}
