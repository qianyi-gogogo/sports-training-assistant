
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.dto.request.auth.UserRegistrationRequest;
import com.qianyi.sportstrainingassistant.dto.response.auth.UserRegistrationResponse;
import com.qianyi.sportstrainingassistant.entity.UserEntity;
import com.qianyi.sportstrainingassistant.repository.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.NoSuchElementException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Service
public class UserAccountService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // ==================== Constructor ====================

    public UserAccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    // ==================== Register ====================

    @Transactional
    public UserRegistrationResponse register(
            UserRegistrationRequest request
    ) {
        if (request == null
                || request.username() == null
                || request.email() == null
                || request.password() == null) {

            throw new IllegalArgumentException(
                    "Username, email and password are required"
            );
        }

        // Normalize username and email.
        String username = request.username()
                .toLowerCase(Locale.ROOT);

        String email = request.email()
                .trim()
                .toLowerCase(Locale.ROOT);

        String password = request.password();

        // ==================== Validate ====================

        if (!username.matches("^[a-z0-9_]{3,32}$")) {
            throw new IllegalArgumentException(
                    "Invalid username"
            );
        }

        if (email.isBlank() || email.length() > 254
                || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException(
                    "Invalid email"
            );
        }

        // BCrypt supports passwords up to 72 UTF-8 bytes.
        if (password.length() < 8
                || password.length() > 72
                || password.getBytes(StandardCharsets.UTF_8).length > 72) {

            throw new IllegalArgumentException(
                    "Password length must be 8-72 characters and at most 72 UTF-8 bytes"
            );
        }

        // ==================== Check Duplicates ====================

        if (userRepository.existsByUsername(username)) {
            throw new IllegalStateException(
                    "Username already exists"
            );
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException(
                    "Email already exists"
            );
        }

        // ==================== Password Hashing ====================

        String passwordHash = passwordEncoder.encode(password);

        // ==================== Save User ====================

        UserEntity user = new UserEntity(
                username,
                email,
                passwordHash
        );

        UserEntity saved;

        try {
            saved = userRepository.saveAndFlush(user);
        } catch (DataIntegrityViolationException ex) {
            // Handle concurrent duplicate registration attempts.
            throw new IllegalStateException(
                    "Username or email already exists",
                    ex
            );
        }

        // ==================== Response ====================

        return new UserRegistrationResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.isEmailVerified()
        );
    }

    // ==================== Current User ====================

    @Transactional(readOnly = true)
    public UserRegistrationResponse getCurrentUser(
            String username
    ) {

        // Find the account associated with the login session.
        UserEntity user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Current user not found"
                        )
                );

        // Return public account information only.
        return new UserRegistrationResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.isEmailVerified()
        );
    }

}
