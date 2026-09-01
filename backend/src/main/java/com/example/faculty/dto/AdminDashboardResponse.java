package com.example.faculty.dto;

public record AdminDashboardResponse(
        long totalFaculty,
        long totalDocuments,
        long pendingDocuments,
        long approvedDocuments,
        long rejectedDocuments
) {}
