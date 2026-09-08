package com.example.faculty.service.impl;

import com.example.faculty.dto.ExperienceRequest;
import com.example.faculty.dto.ExperienceResponse;
import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.Experience;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.entity.User;
import com.example.faculty.exception.ForbiddenException;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.mapper.DocumentMapper;
import com.example.faculty.repository.ExperienceRepository;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.service.AuditLogService;
import com.example.faculty.service.ExperienceService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final FacultyProfileRepository facultyProfileRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public List<ExperienceResponse> getMyExperience(User currentUser) {
        return experienceRepository.findByFacultyUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }
    @Override
    @Transactional
    public ExperienceResponse addExperience(User currentUser, ExperienceRequest request) {
        validateDates(request);
        FacultyProfile profile = getProfile(currentUser);

        Experience experience = Experience.builder()
                .faculty(profile)
                .organization(request.getOrganization())
                .designation(request.getDesignation())
                .department(request.getDepartment())
                .startDate(request.getStartDate())
                .endDate(request.isCurrentlyWorking() ? null : request.getEndDate())
                .currentlyWorking(request.isCurrentlyWorking())
                .description(request.getDescription())
                .build();

        experience = experienceRepository.save(experience);

        auditLogService.log(currentUser, AuditAction.ADD_EXPERIENCE, null,
                "Added experience at " + request.getOrganization());

        return toResponse(experience);
    }

    @Override
    @Transactional
    public ExperienceResponse updateExperience(User currentUser, Long experienceId, ExperienceRequest request) {
        validateDates(request);
        Experience experience = getOwnedExperience(currentUser, experienceId);

        experience.setOrganization(request.getOrganization());
        experience.setDesignation(request.getDesignation());
        experience.setDepartment(request.getDepartment());
        experience.setStartDate(request.getStartDate());
        experience.setEndDate(request.isCurrentlyWorking() ? null : request.getEndDate());
        experience.setCurrentlyWorking(request.isCurrentlyWorking());
        experience.setDescription(request.getDescription());

        experience = experienceRepository.save(experience);

        auditLogService.log(currentUser, AuditAction.UPDATE_EXPERIENCE, null,
                "Updated experience record " + experienceId);

        return toResponse(experience);
    }

    @Override
    @Transactional
    public void deleteExperience(User currentUser, Long experienceId) {
        Experience experience = getOwnedExperience(currentUser, experienceId);
        experienceRepository.delete(experience);

        auditLogService.log(currentUser, AuditAction.DELETE_EXPERIENCE, null,
                "Deleted experience record " + experienceId);
    }

    private void validateDates(ExperienceRequest request) {
        if (!request.isCurrentlyWorking() && request.getEndDate() != null
                && request.getStartDate() != null
                && request.getEndDate().isBefore(request.getStartDate())) {
            throw new ValidationException("Start date cannot be after end date");
        }
    }

    private FacultyProfile getProfile(User currentUser) {
        return facultyProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty profile not found"));
    }

    private Experience getOwnedExperience(User currentUser, Long experienceId) {
        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new ResourceNotFoundException("Experience record not found"));

        if (!experience.getFaculty().getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You do not have access to this experience record");
        }

        return experience;
    }

    private ExperienceResponse toResponse(Experience experience) {
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
