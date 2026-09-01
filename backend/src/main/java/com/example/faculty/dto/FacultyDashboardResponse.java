package com.example.faculty.dto;

public record FacultyDashboardResponse(
        int profileCompletionPercent,
        long educationCount,
        long experienceCount,
        long approvedDocuments,
        long pendingDocuments,
        long rejectedDocuments
) {}
