package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.PasswordResetTokenEntity;
import com.qianyi.sportstrainingassistant.entity.UserEntity;
import com.qianyi.sportstrainingassistant.repository.PasswordResetTokenRepository;
import com.qianyi.sportstrainingassistant.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HexFormat;
import java.util.Locale;

@Service
public class PasswordResetService {

    private static final int TOKEN_BYTES = 32;
    private static final int VALID_MINUTES = 20;
    private static final int COOLDOWN_MINUTES = 2;

    private final UserRepository users;
    private final PasswordResetTokenRepository tokens;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final String publicBaseUrl;
    private final SecureRandom secureRandom = new SecureRandom();

    public PasswordResetService(
            UserRepository users,
            PasswordResetTokenRepository tokens,
            EmailService emailService,
            PasswordEncoder passwordEncoder,
            @Value("${app.public-base-url:http://localhost:8080}") String publicBaseUrl
    ) {
        this.users = users;
        this.tokens = tokens;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.publicBaseUrl = publicBaseUrl.replaceAll("/+$", "");
    }

    // Never disclose if a given email address has an account.
    @Transactional
    public void requestReset(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        String normalized = email.trim().toLowerCase(Locale.ROOT);
        var foundUser = users.findByEmail(normalized);
        if (foundUser.isEmpty()) {
            return;
        }

        UserEntity user = foundUser.get();
        LocalDateTime now = LocalDateTime.now();
        var existing = tokens.findByUserId(user.getId());

        // Rate-limit repeated messages to an individual account.
        if (existing.isPresent()
                && existing.get().getRequestedAt().isAfter(
                        now.minusMinutes(COOLDOWN_MINUTES))) {
            return;
        }

        byte[] bytes = new byte[TOKEN_BYTES];
        secureRandom.nextBytes(bytes);
        String rawToken = Base64.getUrlEncoder()
                .withoutPadding().encodeToString(bytes);

        String tokenHash = sha256(rawToken);
        LocalDateTime expiry = now.plusMinutes(VALID_MINUTES);

        PasswordResetTokenEntity entity;
        if (existing.isPresent()) {
            entity = existing.get();
            entity.rotate(tokenHash, expiry, now);
        } else {
            entity = new PasswordResetTokenEntity(
                    user.getId(), tokenHash, expiry, now);
        }
        tokens.saveAndFlush(entity);

        // The secret appears in the URL fragment, not in the HTTP request path/query.
        String link = publicBaseUrl
                + "/reset-password.html#token=" + rawToken;

        emailService.sendEmail(
                user.getEmail(),
                "Sports Training Assistant - Reset your password",
                "A password reset was requested for your account.\n\n"
                        + "Use this link within 20 minutes:\n" + link
                        + "\n\nIf you did not request this, ignore the message."
        );
    }

    @Transactional
    public void confirmReset(String rawToken, String newPassword) {
        if (rawToken == null || !rawToken.matches("[A-Za-z0-9_-]{43}")) {
            throw new IllegalArgumentException("Invalid or expired reset link");
        }
        if (newPassword == null
                || newPassword.length() < 8
                || newPassword.length() > 72
                || newPassword.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException(
                    "Password must be 8-72 characters and at most 72 UTF-8 bytes");
        }

        PasswordResetTokenEntity reset = tokens
                .findByTokenHash(sha256(rawToken))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid or expired reset link"));

        if (!reset.getExpiresAt().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired reset link");
        }

        UserEntity user = users.findById(reset.getUserId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Invalid or expired reset link"));

        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException(
                    "New password must differ from your old password");
        }

        user.changePasswordHash(passwordEncoder.encode(newPassword));
        users.save(user);

        // A token may only be used once.
        tokens.delete(reset);
        tokens.flush();
    }

    private static String sha256(String value) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(value.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 is unavailable", ex);
        }
    }
}
