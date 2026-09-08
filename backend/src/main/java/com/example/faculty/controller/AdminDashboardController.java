package com.example.faculty.controller;

import com.example.faculty.dto.AdminDashboardResponse;
import com.example.faculty.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminService adminService;

    @GetMapping
    public AdminDashboardResponse getDashboard() {
        return adminService.getDashboard();
    }
}
