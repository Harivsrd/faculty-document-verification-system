package com.example.faculty.service.impl;

import com.example.faculty.dto.FacultyDashboardResponse;
import com.example.faculty.entity.DocumentStatus;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.entity.User;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.repository.DocumentRepository;
import com.example.faculty.repository.EducationRepository;
import com.example.faculty.repository.ExperienceRepository;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.service.FacultyDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class FacultyDashboardServiceImpl implements FacultyDashboardService {

    private final FacultyProfileRepository facultyProfileRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final DocumentRepository documentRepository;

    // Fields considered for the profile-completion percentage.
    private static final List<Predicate<FacultyProfile>> COMPLETION_CHECKS = List.of(
            p -> notBlank(p.getFullName()),
            p -> notBlank(p.getPhone()),
            p -> p.getDateOfBirth() != null,
            p -> notBlank(p.getGender()),
            p -> notBlank(p.getAddress()),
            p -> notBlank(p.getCity()),
            p -> notBlank(p.getState()),
            p -> notBlank(p.getSpecialization()),
            p -> notBlank(p.getDepartment())
    );

    @Override
    public FacultyDashboardResponse getMyDashboard(User currentUser) {
        FacultyProfile profile = facultyProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty profile not found"));

        long educationCount = educationRepository.findByFacultyId(profile.getId()).size();
        long experienceCount = experienceRepository.findByFacultyId(profile.getId()).size();

        List<com.example.faculty.entity.Document> documents = documentRepository.findByFacultyId(profile.getId());
        long approved = documents.stream().filter(d -> d.getStatus() == DocumentStatus.APPROVED).count();
        long pending = documents.stream().filter(d -> d.getStatus() == DocumentStatus.PENDING).count();
        long rejected = documents.stream().filter(d -> d.getStatus() == DocumentStatus.REJECTED).count();

        long satisfied = COMPLETION_CHECKS.stream().filter(check -> check.test(profile)).count();
        int completionPercent = (int) Math.round((satisfied * 100.0) / COMPLETION_CHECKS.size());

        return FacultyDashboardResponse.builder()
                .profileCompletionPercent(completionPercent)
                .educationCount(educationCount)
                .experienceCount(experienceCount)
                .approvedDocuments(approved)
                .pendingDocuments(pending)
                .rejectedDocuments(rejected)
                .build();
    }

    private static boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}
