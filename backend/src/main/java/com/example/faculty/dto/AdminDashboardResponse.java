package com.example.faculty.dto;

<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminDashboardResponse {
    private long totalFaculty;
    private long totalDocuments;
    private long pendingDocuments;
    private long approvedDocuments;
    private long rejectedDocuments;
    private List<DocumentResponse> recentPending;
}
=======
public record AdminDashboardResponse(
        long totalFaculty,
        long totalDocuments,
        long pendingDocuments,
        long approvedDocuments,
        long rejectedDocuments
) {}
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
