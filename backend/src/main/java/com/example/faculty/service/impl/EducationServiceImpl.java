package com.example.faculty.service.impl;

import com.example.faculty.dto.EducationRequest;
import com.example.faculty.dto.EducationResponse;
import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.Education;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.entity.User;
import com.example.faculty.exception.ForbiddenException;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.mapper.DocumentMapper;
import com.example.faculty.repository.EducationRepository;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.service.AuditLogService;
import com.example.faculty.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final FacultyProfileRepository facultyProfileRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional(readOnly = true)
    public List<EducationResponse> getMyEducation(User currentUser) {
        return educationRepository.findByFacultyUserId(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public EducationResponse addEducation(User currentUser, EducationRequest request) {
        FacultyProfile profile = getProfile(currentUser);

        Education education = Education.builder()
                .faculty(profile)
                .qualificationType(request.getQualificationType())
                .institution(request.getInstitution())
                .boardOrUniversity(request.getBoardOrUniversity())
                .courseName(request.getCourseName())
                .yearOfPassing(request.getYearOfPassing())
                .percentageOrCgpa(request.getPercentageOrCgpa())
                .build();

        education = educationRepository.save(education);

        auditLogService.log(currentUser, AuditAction.ADD_EDUCATION, null,
                "Added " + request.getQualificationType() + " qualification");

        return toResponse(education);
    }

    @Override
    @Transactional
    public EducationResponse updateEducation(User currentUser, Long educationId, EducationRequest request) {
        Education education = getOwnedEducation(currentUser, educationId);

        education.setQualificationType(request.getQualificationType());
        education.setInstitution(request.getInstitution());
        education.setBoardOrUniversity(request.getBoardOrUniversity());
        education.setCourseName(request.getCourseName());
        education.setYearOfPassing(request.getYearOfPassing());
        education.setPercentageOrCgpa(request.getPercentageOrCgpa());

        education = educationRepository.save(education);

        auditLogService.log(currentUser, AuditAction.UPDATE_EDUCATION, null,
                "Updated education record " + educationId);

        return toResponse(education);
    }

    @Override
    @Transactional
    public void deleteEducation(User currentUser, Long educationId) {
        Education education = getOwnedEducation(currentUser, educationId);
        educationRepository.delete(education);

        auditLogService.log(currentUser, AuditAction.DELETE_EDUCATION, null,
                "Deleted education record " + educationId);
    }

    private FacultyProfile getProfile(User currentUser) {
        return facultyProfileRepository.findByUserId(currentUser.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty profile not found"));
    }

    /**
     * Fetches an education record and verifies it belongs to the current user.
     * This is the enforcement point that keeps faculty from touching each
     * other's records even if they guess another record's id.
     */
    private Education getOwnedEducation(User currentUser, Long educationId) {
        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new ResourceNotFoundException("Education record not found"));

        if (!education.getFaculty().getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You do not have access to this education record");
        }

        return education;
    }

    private EducationResponse toResponse(Education education) {
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
}
