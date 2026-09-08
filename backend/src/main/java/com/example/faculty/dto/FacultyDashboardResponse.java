package com.example.faculty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDashboardResponse {
    private int profileCompletionPercent;
    private long educationCount;
    private long experienceCount;
    private long approvedDocuments;
    private long pendingDocuments;
    private long rejectedDocuments;
}
