package com.example.faculty.controller;

import com.example.faculty.dto.FacultyDashboardResponse;
import com.example.faculty.entity.User;
import com.example.faculty.service.FacultyDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FacultyDashboardController {

    private final FacultyDashboardService facultyDashboardService;

    @GetMapping("/api/faculty/dashboard")
    public FacultyDashboardResponse getMyDashboard(@AuthenticationPrincipal User currentUser) {
        return facultyDashboardService.getMyDashboard(currentUser);
    }
}
