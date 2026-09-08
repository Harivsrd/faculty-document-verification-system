package com.example.faculty.security;

import com.example.faculty.entity.Role;
import com.example.faculty.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    // 32-byte base64 secret, matches the default in application.yml
    private final JwtService jwtService = new JwtService(
            "c2VjdXJlLWRldi1vbmx5LWp3dC1zZWNyZXQtY2hhbmdlLW1lLWluLXByb2R1Y3Rpb24tMzItYnl0ZXM=",
            86400000L
    );

    private UserPrincipal buildPrincipal() {
        User user = User.builder()
                .id(1L)
                .name("Test Faculty")
                .email("test@example.com")
                .password("hashed")
                .role(Role.FACULTY)
                .enabled(true)
                .build();
        return new UserPrincipal(user);
    }

    @Test
    void generatesTokenAndExtractsEmail() {
        UserPrincipal principal = buildPrincipal();
        String token = jwtService.generateToken(principal);

        assertNotNull(token);
        assertEquals("test@example.com", jwtService.extractEmail(token));
        assertEquals(1L, jwtService.extractUserId(token));
    }

    @Test
    void validatesTokenForMatchingEmail() {
        UserPrincipal principal = buildPrincipal();
        String token = jwtService.generateToken(principal);

        assertTrue(jwtService.isTokenValid(token, "test@example.com"));
        assertFalse(jwtService.isTokenValid(token, "someone-else@example.com"));
    }

    @Test
    void rejectsMalformedToken() {
        assertFalse(jwtService.isTokenValid("not-a-real-token", "test@example.com"));
    }
}
