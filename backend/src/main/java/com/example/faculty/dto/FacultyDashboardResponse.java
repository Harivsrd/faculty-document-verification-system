package com.example.faculty.dto;

<<<<<<< HEAD
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
=======
public record FacultyDashboardResponse(
        int profileCompletionPercent,
        long educationCount,
        long experienceCount,
        long approvedDocuments,
        long pendingDocuments,
        long rejectedDocuments
) {}
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
