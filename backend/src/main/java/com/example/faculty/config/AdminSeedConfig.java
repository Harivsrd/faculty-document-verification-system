package com.example.faculty.config;

import com.example.faculty.entity.Role;
import com.example.faculty.entity.User;
import com.example.faculty.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Development/ops admin seed mechanism.
 *
 * Public registration (POST /api/auth/register) always creates FACULTY users.
 * The only way to obtain an ADMIN account is:
 *   1. This seed, controlled entirely by server-side environment configuration
 *      (app.admin.seed.* / APP_ADMIN_* env vars), or
 *   2. A direct database operation performed by an operator with DB access.
 *
 * This keeps privilege escalation impossible from the public API surface.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeedConfig implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.seed.enabled}")
    private boolean seedEnabled;

    @Value("${app.admin.seed.name}")
    private String seedName;

    @Value("${app.admin.seed.email}")
    private String seedEmail;

    @Value("${app.admin.seed.password}")
    private String seedPassword;

    @Override
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }

        boolean anyAdminExists = userRepository.findByEmail(seedEmail).isPresent();
        if (anyAdminExists) {
            return;
        }

        User admin = User.builder()
                .name(seedName)
                .email(seedEmail)
                .password(passwordEncoder.encode(seedPassword))
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        userRepository.save(admin);
        log.warn("Seeded default ADMIN account ({}). Change its password immediately in production.", seedEmail);
    }
}
