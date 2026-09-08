package com.example.faculty.controller;

import com.example.faculty.dto.LoginRequest;
import com.example.faculty.dto.LoginResponse;
import com.example.faculty.dto.RegisterRequest;
<<<<<<< HEAD
import com.example.faculty.dto.UserResponse;
=======
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
import com.example.faculty.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
<<<<<<< HEAD
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
=======
    public ResponseEntity<LoginResponse> register(@Valid @RequestBody RegisterRequest request) {
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
