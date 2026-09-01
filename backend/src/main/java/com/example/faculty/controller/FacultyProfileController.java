package com.example.faculty.controller;

import com.example.faculty.dto.FacultyDashboardResponse;
import com.example.faculty.dto.FacultyProfileRequest;
import com.example.faculty.dto.FacultyProfileResponse;
import com.example.faculty.entity.User;
import com.example.faculty.repository.DocumentRepository;
import com.example.faculty.repository.EducationRepository;
import com.example.faculty.repository.ExperienceRepository;
import com.example.faculty.entity.DocumentStatus;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.service.FacultyProfileService;
import com.example.faculty.util.CurrentUserProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculty/profile")
@RequiredArgsConstructor
public class FacultyProfileController {

    private final FacultyProfileService facultyProfileService;
    private final CurrentUserProvider currentUserProvider;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final DocumentRepository documentRepository;

    @GetMapping
    public FacultyProfileResponse getProfile() {
        User user = currentUserProvider.getCurrentUser();
        return facultyProfileService.getMyProfile(user);
    }

    @PutMapping
    public FacultyProfileResponse updateProfile(@Valid @RequestBody FacultyProfileRequest request) {
        User user = currentUserProvider.getCurrentUser();
        return facultyProfileService.updateMyProfile(user, request);
    }

    @GetMapping("/dashboard")
    public FacultyDashboardResponse getDashboard() {
        User user = currentUserProvider.getCurrentUser();
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);

        long educationCount = educationRepository.findByFaculty_Id(profile.getId()).size();
        long experienceCount = experienceRepository.findByFaculty_Id(profile.getId()).size();
        long approved = documentRepository.findByFaculty_IdAndStatus(profile.getId(), DocumentStatus.APPROVED).size();
        long pending = documentRepository.findByFaculty_IdAndStatus(profile.getId(), DocumentStatus.PENDING).size();
        long rejected = documentRepository.findByFaculty_IdAndStatus(profile.getId(), DocumentStatus.REJECTED).size();

        int completion = computeCompletion(profile);

        return new FacultyDashboardResponse(completion, educationCount, experienceCount, approved, pending, rejected);
    }

    private int computeCompletion(FacultyProfile p) {
        String[] fields = {
                p.getFullName(), p.getPhone(), p.getGender(), p.getAddress(), p.getCity(),
                p.getState(), p.getSpecialization(), p.getDepartment()
        };
        long filled = 0;
        for (String f : fields) {
            if (f != null && !f.isBlank()) filled++;
        }
        double dobBonus = p.getDateOfBirth() != null ? 1 : 0;
        double total = fields.length + 1;
        return (int) Math.round(((filled + dobBonus) / total) * 100);
    }
}
