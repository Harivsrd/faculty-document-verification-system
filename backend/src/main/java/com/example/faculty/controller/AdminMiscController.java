package com.example.faculty.controller;

import com.example.faculty.dto.AdminDashboardResponse;
import com.example.faculty.dto.AuditLogResponse;
import com.example.faculty.service.AdminDashboardService;
import com.example.faculty.service.AuditLogQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminMiscController {

    private final AdminDashboardService adminDashboardService;
    private final AuditLogQueryService auditLogQueryService;

    @GetMapping("/api/admin/dashboard")
    public AdminDashboardResponse getDashboard() {
        return adminDashboardService.getDashboard();
    }

    @GetMapping("/api/admin/audit-logs")
    public Page<AuditLogResponse> getAuditLogs(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String action,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("timestamp").descending());
        return auditLogQueryService.filter(userId, action, pageable);
    }
}
