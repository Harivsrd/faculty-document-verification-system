package com.example.faculty.service;

import com.example.faculty.dto.LoginRequest;
import com.example.faculty.dto.LoginResponse;
import com.example.faculty.dto.RegisterRequest;
import com.example.faculty.dto.UserResponse;

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
