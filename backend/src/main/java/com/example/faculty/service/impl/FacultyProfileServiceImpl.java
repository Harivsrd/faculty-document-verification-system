package com.example.faculty.service.impl;

import com.example.faculty.dto.FacultyProfileRequest;
import com.example.faculty.dto.FacultyProfileResponse;
import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.entity.User;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.service.AuditLogService;
import com.example.faculty.service.FacultyProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FacultyProfileServiceImpl implements FacultyProfileService {

    private final FacultyProfileRepository facultyProfileRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public FacultyProfileResponse getMyProfile(User currentUser) {
        FacultyProfile profile = getOrCreateProfile(currentUser);
        return toResponse(profile);
    }

    @Override
    @Transactional
    public FacultyProfileResponse updateMyProfile(User currentUser, FacultyProfileRequest request) {
        FacultyProfile profile = getOrCreateProfile(currentUser);

        profile.setFullName(request.getFullName());
        profile.setPhone(request.getPhone());
        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setGender(request.getGender());
        profile.setAddress(request.getAddress());
        profile.setCity(request.getCity());
        profile.setState(request.getState());
        profile.setSpecialization(request.getSpecialization());
        profile.setDepartment(request.getDepartment());

        profile = facultyProfileRepository.save(profile);

        auditLogService.log(currentUser, AuditAction.UPDATE_PROFILE, null, "Faculty profile updated");

        return toResponse(profile);
    }

    /**
     * Registration always creates a profile, but this guards against
     * legacy/edge-case users (e.g. seeded admins) that don't have one.
     */
    private FacultyProfile getOrCreateProfile(User currentUser) {
        return facultyProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty profile not found"));
    }

    public static FacultyProfileResponse toResponse(FacultyProfile profile) {
        return FacultyProfileResponse.builder()
                .id(profile.getId())
                .fullName(profile.getFullName())
                .email(profile.getUser().getEmail())
                .phone(profile.getPhone())
                .dateOfBirth(profile.getDateOfBirth())
                .gender(profile.getGender())
                .address(profile.getAddress())
                .city(profile.getCity())
                .state(profile.getState())
                .specialization(profile.getSpecialization())
                .department(profile.getDepartment())
                .profilePhoto(profile.getProfilePhoto())
                .build();
    }
}
