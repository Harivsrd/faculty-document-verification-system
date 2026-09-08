package com.example.faculty.service;

import com.example.faculty.dto.LoginRequest;
import com.example.faculty.dto.LoginResponse;
import com.example.faculty.dto.RegisterRequest;
import com.example.faculty.dto.UserResponse;
<<<<<<< HEAD

public interface AuthService {
    UserResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
=======
import com.example.faculty.entity.AuditAction;
import com.example.faculty.entity.Role;
import com.example.faculty.entity.User;
import com.example.faculty.exception.UserAlreadyExistsException;
import com.example.faculty.repository.UserRepository;
import com.example.faculty.security.JwtService;
import com.example.faculty.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuditService auditService;

    @Transactional
    public LoginResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new UserAlreadyExistsException("An account with this email already exists");
        }

        // Public registration always creates FACULTY accounts. ADMIN accounts can only be
        // created via the server-side seed mechanism or direct database access.
        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.FACULTY)
                .enabled(true)
                .build();

        user = userRepository.save(user);
        auditService.log(user, AuditAction.REGISTER, "New faculty account registered: " + user.getEmail());

        UserPrincipal principal = new UserPrincipal(user);
        String token = jwtService.generateToken(principal);
        return LoginResponse.of(token, UserResponse.from(user));
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        auditService.log(user, AuditAction.LOGIN, "User logged in: " + user.getEmail());

        UserPrincipal principal = new UserPrincipal(user);
        String token = jwtService.generateToken(principal);
        return LoginResponse.of(token, UserResponse.from(user));
    }
>>>>>>> b100b436eab738f8f9eca6812bdd01701ec097b3
}
