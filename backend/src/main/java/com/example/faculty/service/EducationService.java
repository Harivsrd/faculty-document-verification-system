package com.example.faculty.service;

import com.example.faculty.dto.EducationRequest;
import com.example.faculty.dto.EducationResponse;
import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.Education;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.entity.User;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.repository.EducationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EducationService {

    private final EducationRepository educationRepository;
    private final FacultyProfileService facultyProfileService;
    private final AuditService auditService;

    @Transactional(readOnly = true)
    public List<EducationResponse> listMine(User user) {
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);
        return educationRepository.findByFaculty_Id(profile.getId())
                .stream().map(EducationResponse::from).toList();
    }

    @Transactional
    public EducationResponse create(User user, EducationRequest request) {
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);

        Education education = Education.builder()
                .faculty(profile)
                .qualificationType(request.qualificationType())
                .institution(request.institution())
                .boardOrUniversity(request.boardOrUniversity())
                .courseName(request.courseName())
                .yearOfPassing(request.yearOfPassing())
                .percentageOrCgpa(request.percentageOrCgpa())
                .build();

        Education saved = educationRepository.save(education);
        auditService.log(user, AuditAction.ADD_EDUCATION,
                "Added education record: " + request.qualificationType());
        return EducationResponse.from(saved);
    }

    @Transactional
    public EducationResponse update(User user, Long educationId, EducationRequest request) {
        Education education = getOwned(user, educationId);

        education.setQualificationType(request.qualificationType());
        education.setInstitution(request.institution());
        education.setBoardOrUniversity(request.boardOrUniversity());
        education.setCourseName(request.courseName());
        education.setYearOfPassing(request.yearOfPassing());
        education.setPercentageOrCgpa(request.percentageOrCgpa());

        Education saved = educationRepository.save(education);
        auditService.log(user, AuditAction.UPDATE_EDUCATION, "Updated education record #" + educationId);
        return EducationResponse.from(saved);
    }

    @Transactional
    public void delete(User user, Long educationId) {
        Education education = getOwned(user, educationId);
        educationRepository.delete(education);
        auditService.log(user, AuditAction.DELETE_EDUCATION, "Deleted education record #" + educationId);
    }

    /** Package-visible so DocumentService can attach certificates to a verified-owned education record. */
    @Transactional(readOnly = true)
    public Education getOwned(User user, Long educationId) {
        FacultyProfile profile = facultyProfileService.getOrCreateProfile(user);
        return educationRepository.findByIdAndFaculty_Id(educationId, profile.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Education record not found"));
    }
}
