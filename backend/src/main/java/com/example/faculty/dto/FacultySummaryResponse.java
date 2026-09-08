package com.example.faculty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultySummaryResponse {
    private Long facultyId;
    private String fullName;
    private String email;
    private String department;
    private String specialization;
    private long documentCount;
}
