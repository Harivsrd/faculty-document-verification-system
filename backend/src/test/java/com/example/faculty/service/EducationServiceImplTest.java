package com.example.faculty.service;

import com.example.faculty.dto.EducationRequest;
import com.example.faculty.entity.*;
import com.example.faculty.exception.ForbiddenException;
import com.example.faculty.exception.ResourceNotFoundException;
import com.example.faculty.repository.EducationRepository;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.service.impl.EducationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EducationServiceImplTest {

    @Mock
    private EducationRepository educationRepository;
    @Mock
    private FacultyProfileRepository facultyProfileRepository;
    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private EducationServiceImpl educationService;

    @Test
    void updateEducation_throwsForbidden_whenRecordBelongsToAnotherFaculty() {
        User owner = User.builder().id(1L).email("owner@example.com").role(Role.FACULTY).build();
        User intruder = User.builder().id(2L).email("intruder@example.com").role(Role.FACULTY).build();

        FacultyProfile ownerProfile = FacultyProfile.builder().id(10L).user(owner).build();
        Education education = Education.builder()
                .id(100L)
                .faculty(ownerProfile)
                .qualificationType(QualificationType.PHD)
                .institution("Original University")
                .build();

        when(educationRepository.findById(100L)).thenReturn(Optional.of(education));

        EducationRequest request = new EducationRequest();
        request.setQualificationType(QualificationType.PHD);
        request.setInstitution("Hijacked University");

        assertThatThrownBy(() -> educationService.updateEducation(intruder, 100L, request))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    void updateEducation_throwsNotFound_whenRecordDoesNotExist() {
        when(educationRepository.findById(999L)).thenReturn(Optional.empty());

        User user = User.builder().id(1L).email("owner@example.com").role(Role.FACULTY).build();
        EducationRequest request = new EducationRequest();
        request.setQualificationType(QualificationType.MASTERS);
        request.setInstitution("Some University");

        assertThatThrownBy(() -> educationService.updateEducation(user, 999L, request))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
