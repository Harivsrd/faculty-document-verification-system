package com.example.faculty.dto;

import com.example.faculty.entity.DocumentStatus;
<<<<<<< HEAD
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerificationHistoryResponse {
    private Long id;
    private Long documentId;
    private String adminName;
    private DocumentStatus previousStatus;
    private DocumentStatus newStatus;
    private String comment;
    private LocalDateTime actionTimestamp;
=======
import com.example.faculty.entity.VerificationHistory;

import java.time.LocalDateTime;

public record VerificationHistoryResponse(
        Long id,
        Long documentId,
        String adminName,
        DocumentStatus previousStatus,
        DocumentStatus newStatus,
        String comment,
        LocalDateTime actionTimestamp
) {
    public static VerificationHistoryResponse from(VerificationHistory h) {
        return new VerificationHistoryResponse(
                h.getId(),
                h.getDocument().getId(),
                h.getAdmin() != null ? h.getAdmin().getName() : "System",
                h.getPreviousStatus(),
                h.getNewStatus(),
                h.getComment(),
                h.getActionTimestamp()
        );
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
