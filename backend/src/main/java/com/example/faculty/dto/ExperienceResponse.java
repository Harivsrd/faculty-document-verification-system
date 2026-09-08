package com.example.faculty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExperienceResponse {
    private Long id;
    private String organization;
    private String designation;
    private String department;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean currentlyWorking;
    private String description;
    private List<DocumentResponse> documents;
}
