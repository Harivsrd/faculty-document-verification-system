package com.example.faculty.service.impl;

import com.example.faculty.dto.LoginRequest;
import com.example.faculty.dto.LoginResponse;
import com.example.faculty.dto.RegisterRequest;
import com.example.faculty.dto.UserResponse;
import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.FacultyProfile;
import com.example.faculty.entity.Role;
import com.example.faculty.entity.User;
import com.example.faculty.exception.UserAlreadyExistsException;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.repository.UserRepository;
import com.example.faculty.security.JwtService;
import com.example.faculty.service.AuditLogService;
import com.example.faculty.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final FacultyProfileRepository facultyProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("An account with this email already exists");
        }

        // Public registration always creates FACULTY users.
        // Admin accounts are provisioned only via AdminSeeder.
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.FACULTY)
                .enabled(true)
                .build();

        user = userRepository.save(user);

        FacultyProfile profile = FacultyProfile.builder()
                .user(user)
                .fullName(request.getName())
                .build();
        facultyProfileRepository.save(profile);

        auditLogService.log(user, AuditAction.CREATE_PROFILE, null, "Faculty account registered");

        return toUserResponse(user);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("User vanished after authentication"));

        String token = jwtService.generateToken(user);

        auditLogService.log(user, AuditAction.LOGIN, null, "User logged in");

        return LoginResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .user(toUserResponse(user))
                .build();
    }

    private UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
