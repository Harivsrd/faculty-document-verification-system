package com.example.faculty.service;

import com.example.faculty.dto.FacultyDashboardResponse;
import com.example.faculty.entity.User;

public interface FacultyDashboardService {
    FacultyDashboardResponse getMyDashboard(User currentUser);
}
