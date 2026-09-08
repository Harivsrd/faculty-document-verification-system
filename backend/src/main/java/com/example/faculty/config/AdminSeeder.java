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
 * Seeds a single ADMIN account on application startup.
 * Admin accounts are intentionally NOT creatable through the public
 * /api/auth/register endpoint. This is the only supported way to
 * provision the first administrator; further admins can be promoted
 * directly in the database or added here as needed.
 *
 * Disable in production by setting app.admin.seed.enabled=false and
 * always override the default seed password via ADMIN_SEED_PASSWORD.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.seed.enabled}")
    private boolean seedEnabled;

    @Value("${app.admin.seed.email}")
    private String seedEmail;

    @Value("${app.admin.seed.password}")
    private String seedPassword;

    @Value("${app.admin.seed.name}")
    private String seedName;

    @Override
    public void run(String... args) {
        if (!seedEnabled) {
            return;
        }

        boolean adminExists = userRepository.findByEmail(seedEmail).isPresent();
        if (adminExists) {
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
        log.warn("Seeded default ADMIN account ({}). Change the password immediately after first login.", seedEmail);
    }
}
