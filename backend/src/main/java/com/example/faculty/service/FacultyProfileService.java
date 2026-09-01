package com.example.faculty.service;

import com.example.faculty.dto.FacultyProfileRequest;
import com.example.faculty.dto.FacultyProfileResponse;
import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.entity.User;
import com.example.faculty.repository.FacultyProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacultyProfileService {

    private final FacultyProfileRepository facultyProfileRepository;
    private final AuditService auditService;

    /** Returns the current user's profile, creating an empty one on first access. */
    @Transactional
    public FacultyProfile getOrCreateProfile(User user) {
        return facultyProfileRepository.findByUser_Id(user.getId())
                .orElseGet(() -> {
                    FacultyProfile profile = FacultyProfile.builder()
                            .user(user)
                            .fullName(user.getName())
                            .build();
                    FacultyProfile saved = facultyProfileRepository.save(profile);
                    auditService.log(user, AuditAction.CREATE_PROFILE, "Faculty profile initialized");
                    return saved;
                });
    }

    @Transactional(readOnly = true)
    public FacultyProfileResponse getMyProfile(User user) {
        return FacultyProfileResponse.from(getOrCreateProfile(user));
    }

    @Transactional
    public FacultyProfileResponse updateMyProfile(User user, FacultyProfileRequest request) {
        FacultyProfile profile = getOrCreateProfile(user);

        profile.setFullName(request.fullName());
        profile.setPhone(request.phone());
        profile.setDateOfBirth(request.dateOfBirth());
        profile.setGender(request.gender());
        profile.setAddress(request.address());
        profile.setCity(request.city());
        profile.setState(request.state());
        profile.setSpecialization(request.specialization());
        profile.setDepartment(request.department());
        if (request.publiclyVisible() != null) {
            profile.setPubliclyVisible(request.publiclyVisible());
        }

        FacultyProfile saved = facultyProfileRepository.save(profile);
        auditService.log(user, AuditAction.UPDATE_PROFILE, "Faculty profile updated");
        return FacultyProfileResponse.from(saved);
    }
}
