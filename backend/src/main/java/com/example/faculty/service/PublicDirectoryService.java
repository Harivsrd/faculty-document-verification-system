package com.example.faculty.service;

import com.example.faculty.dto.EducationResponse;
import com.example.faculty.dto.ExperienceResponse;
import com.example.faculty.dto.PageResponse;
import com.example.faculty.dto.PublicFacultyResponse;
import com.example.faculty.entity.DocumentStatus;
import com.example.faculty.entity.Education;
import com.example.faculty.entity.Experience;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.repository.EducationRepository;
import com.example.faculty.repository.ExperienceRepository;
import com.example.faculty.repository.FacultyProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicDirectoryService {

    private final FacultyProfileRepository facultyProfileRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;

    @Transactional(readOnly = true)
    public PageResponse<PublicFacultyResponse> listApprovedFaculty(Pageable pageable) {
        Page<PublicFacultyResponse> page = facultyProfileRepository.findByPubliclyVisibleTrue(pageable)
                .map(PublicFacultyResponse::from);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public PublicFacultyResponse getApprovedFaculty(Long facultyId) {
        FacultyProfile profile = getVisibleOrThrow(facultyId);
        return PublicFacultyResponse.from(profile);
    }

    /** Only education records that have at least one APPROVED certificate are considered "verified" publicly. */
    @Transactional(readOnly = true)
    public List<EducationResponse> getVerifiedEducation(Long facultyId) {
        getVisibleOrThrow(facultyId);
        return educationRepository.findByFaculty_Id(facultyId).stream()
                .filter(e -> e.getCertificateDocuments().stream()
                        .anyMatch(d -> d.getStatus() == DocumentStatus.APPROVED))
                .map(this::toPublicSafeEducation)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExperienceResponse> getVerifiedExperience(Long facultyId) {
        getVisibleOrThrow(facultyId);
        return experienceRepository.findByFaculty_Id(facultyId).stream()
                .filter(e -> e.getCertificateDocuments().stream()
                        .anyMatch(d -> d.getStatus() == DocumentStatus.APPROVED))
                .map(ExperienceResponse::from)
                .toList();
    }

    private FacultyProfile getVisibleOrThrow(Long facultyId) {
        FacultyProfile profile = facultyProfileRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found"));
        if (!profile.isPubliclyVisible()) {
            throw new ResourceNotFoundException("Faculty not found");
        }
        return profile;
    }

    /** Reuses EducationResponse but the controller/DTO layer never leaks internal document file paths. */
    private EducationResponse toPublicSafeEducation(Education e) {
        return EducationResponse.from(e);
    }
}
