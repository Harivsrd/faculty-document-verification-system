package com.example.faculty.service.impl;

import com.example.faculty.dto.AdminDashboardResponse;
import com.example.faculty.entity.DocumentStatus;
import com.example.faculty.mapper.DocumentMapper;
import com.example.faculty.repository.DocumentRepository;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.service.AdminDashboardService;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final FacultyProfileRepository facultyProfileRepository;
    private final DocumentRepository documentRepository;

    @Override
    @Transactional(readOnly = true)
    public AdminDashboardResponse getDashboard() {
        long pending = documentRepository.countByStatus(DocumentStatus.PENDING);
        long approved = documentRepository.countByStatus(DocumentStatus.APPROVED);
        long rejected = documentRepository.countByStatus(DocumentStatus.REJECTED);

        var recentPending = documentRepository
                .findByStatusWithDetails(
                        DocumentStatus.PENDING,
                        PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "uploadedAt"))
                )
                .map(DocumentMapper::toResponse)
                .getContent();

        return AdminDashboardResponse.builder()
                .totalFaculty(facultyProfileRepository.count())
                .totalDocuments(pending + approved + rejected)
                .pendingDocuments(pending)
                .approvedDocuments(approved)
                .rejectedDocuments(rejected)
                .recentPending(recentPending)
                .build();
    }
}
