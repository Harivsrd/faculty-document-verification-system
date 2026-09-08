package com.example.faculty.service.impl;

import com.example.faculty.dto.PublicFacultyDetailResponse;
import com.example.faculty.dto.PublicFacultyResponse;
import com.example.faculty.entity.Document;
import com.example.faculty.entity.DocumentStatus;
import com.example.faculty.entity.Education;
import com.example.faculty.entity.Experience;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.repository.DocumentRepository;
import com.example.faculty.repository.EducationRepository;
import com.example.faculty.repository.ExperienceRepository;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.service.PublicFacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicFacultyServiceImpl implements PublicFacultyService {

    private final FacultyProfileRepository facultyProfileRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final DocumentRepository documentRepository;

    @Override
    public List<PublicFacultyResponse> searchApprovedFaculty(String keyword) {
        String normalizedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();

        return facultyProfileRepository.search(normalizedKeyword, PageRequest.of(0, 100))
                .stream()
                .filter(this::hasAtLeastOneApprovedDocument)
                .map(p -> PublicFacultyResponse.builder()
                        .facultyId(p.getId())
                        .fullName(p.getFullName())
                        .department(p.getDepartment())
                        .specialization(p.getSpecialization())
                        .city(p.getCity())
                        .state(p.getState())
                        .build())
                .toList();
    }

    @Override
    public PublicFacultyDetailResponse getApprovedFacultyDetail(Long facultyId) {
        FacultyProfile profile = facultyProfileRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found"));

        if (!hasAtLeastOneApprovedDocument(profile)) {
            throw new ResourceNotFoundException("Faculty not found");
        }

        List<String> verifiedQualifications = educationRepository.findByFacultyId(facultyId).stream()
                .filter(e -> hasApprovedDocument(e.getDocuments()))
                .map(e -> e.getQualificationType() + " - " + e.getInstitution())
                .toList();

        List<String> verifiedExperience = experienceRepository.findByFacultyId(facultyId).stream()
                .filter(e -> hasApprovedDocument(e.getDocuments()))
                .map(e -> e.getDesignation() + " at " + e.getOrganization())
                .toList();

        return PublicFacultyDetailResponse.builder()
                .facultyId(profile.getId())
                .fullName(profile.getFullName())
                .department(profile.getDepartment())
                .specialization(profile.getSpecialization())
                .city(profile.getCity())
                .state(profile.getState())
                .verifiedQualifications(verifiedQualifications)
                .verifiedExperience(verifiedExperience)
                .build();
    }

    private boolean hasAtLeastOneApprovedDocument(FacultyProfile profile) {
        return documentRepository.findByFacultyId(profile.getId()).stream()
                .anyMatch(d -> d.getStatus() == DocumentStatus.APPROVED);
    }

    private boolean hasApprovedDocument(List<Document> documents) {
        return documents.stream().anyMatch(d -> d.getStatus() == DocumentStatus.APPROVED);
    }
}
