package com.example.faculty.service;

import com.example.faculty.dto.LoginRequest;
import com.example.faculty.dto.LoginResponse;
import com.example.faculty.dto.RegisterRequest;
import com.example.faculty.dto.UserResponse;
import com.example.faculty.entity.Role;
import com.example.faculty.entity.User;
import com.example.faculty.exception.UserAlreadyExistsException;
import com.example.faculty.repository.FacultyProfileRepository;
import com.example.faculty.repository.UserRepository;
import com.example.faculty.security.JwtService;
import com.example.faculty.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private FacultyProfileRepository facultyProfileRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Ravi Kumar");
        registerRequest.setEmail("ravi.kumar@example.com");
        registerRequest.setPassword("SecurePass123");
    }

    @Test
    void register_createsFacultyUser_whenEmailIsNew() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn("hashed-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserResponse response = authService.register(registerRequest);

        assertThat(response.getEmail()).isEqualTo(registerRequest.getEmail());
        assertThat(response.getRole()).isEqualTo(Role.FACULTY);
        verify(facultyProfileRepository).save(any());
    }

    @Test
    void register_throwsUserAlreadyExists_whenEmailIsTaken() {
        when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(UserAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_returnsTokenAndUser_whenCredentialsAreValid() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("ravi.kumar@example.com");
        loginRequest.setPassword("SecurePass123");

        User user = User.builder()
                .id(1L)
                .name("Ravi Kumar")
                .email(loginRequest.getEmail())
                .password("hashed-password")
                .role(Role.FACULTY)
                .build();

        when(userRepository.findByEmail(loginRequest.getEmail())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(user)).thenReturn("test-jwt-token");

        LoginResponse response = authService.login(loginRequest);

        assertThat(response.getToken()).isEqualTo("test-jwt-token");
        assertThat(response.getUser().getEmail()).isEqualTo(loginRequest.getEmail());
        verify(authenticationManager).authenticate(any());
    }
}
