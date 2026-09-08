package com.example.faculty.service;

import com.example.faculty.dto.ExperienceRequest;
import com.example.faculty.dto.ExperienceResponse;
<<<<<<< HEAD
import com.example.faculty.entity.User;

import java.util.List;

public interface ExperienceService {
    List<ExperienceResponse> getMyExperience(User currentUser);
    ExperienceResponse addExperience(User currentUser, ExperienceRequest request);
    ExperienceResponse updateExperience(User currentUser, Long experienceId, ExperienceRequest request);
    void deleteExperience(User currentUser, Long experienceId);
=======
import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.Experience;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.entity.User;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.repository.ExperienceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final FacultyProfileService facultyProfileService;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public List<ExperienceResponse> listMine(User user) {
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);
        return experienceRepository.findByFaculty_Id(profile.getId())
                .stream().map(ExperienceResponse::from).toList();
    }

    @Transactional
    public ExperienceResponse create(User user, ExperienceRequest request) {
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);

        Experience experience = Experience.builder()
                .faculty(profile)
                .organization(request.organization())
                .designation(request.designation())
                .department(request.department())
                .startDate(request.startDate())
                .endDate(request.currentlyWorking() ? null : request.endDate())
                .currentlyWorking(request.currentlyWorking())
                .description(request.description())
                .build();

        Experience saved = experienceRepository.save(experience);
        auditService.log(user, AuditAction.ADD_EXPERIENCE, "Added experience: " + request.organization());
        return ExperienceResponse.from(saved);
    }

    @Transactional
    public ExperienceResponse update(User user, Long experienceId, ExperienceRequest request) {
        Experience experience = getOwned(user, experienceId);

        experience.setOrganization(request.organization());
        experience.setDesignation(request.designation());
        experience.setDepartment(request.department());
        experience.setStartDate(request.startDate());
        experience.setEndDate(request.currentlyWorking() ? null : request.endDate());
        experience.setCurrentlyWorking(request.currentlyWorking());
        experience.setDescription(request.description());

        Experience saved = experienceRepository.save(experience);
        auditService.log(user, AuditAction.UPDATE_EXPERIENCE, "Updated experience record #" + experienceId);
        return ExperienceResponse.from(saved);
    }

    @Transactional
    public void delete(User user, Long experienceId) {
        Experience experience = getOwned(user, experienceId);
        experienceRepository.delete(experience);
        auditService.log(user, AuditAction.DELETE_EXPERIENCE, "Deleted experience record #" + experienceId);
    }

    @Transactional(readOnly = true)
    public Experience getOwned(User user, Long experienceId) {
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);
        return experienceRepository.findByIdAndFaculty_Id(experienceId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Experience record not found"));
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
