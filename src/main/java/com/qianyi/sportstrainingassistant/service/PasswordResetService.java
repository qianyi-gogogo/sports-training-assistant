package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.PasswordResetCodeEntity;
import com.qianyi.sportstrainingassistant.repository.PasswordResetCodeRepository;
import com.qianyi.sportstrainingassistant.repository.UserRepository;
import com.qianyi.sportstrainingassistant.security.PasswordCredentialStamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class PasswordResetService {

    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int CODE_VALID_MINUTES = 10;
    private static final int RESEND_SECONDS = 60;
    private static final int MAX_SEND_COUNT = 5;
    private static final int MAX_FAILED_ATTEMPTS = 5;

    private final UserRepository users;
    private final PasswordResetCodeRepository codes;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    public PasswordResetService(UserRepository users,
                                PasswordResetCodeRepository codes,
                                EmailService emailService,
                                PasswordEncoder passwordEncoder) {
        this.users = users;
        this.codes = codes;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // ==================== Request Reset Code ====================

    @Transactional
    public void requestReset(String email) {
        String normalized = normalizeEmail(email);

        // Always lock the user before accessing the code record.
        // This serializes resends and confirmations for the same account.
        var found = users.findByEmailForUpdate(normalized);
        if (found.isEmpty() || !found.get().isEmailVerified()) {
            return;
        }
        var user = found.get();
        LocalDateTime now = LocalDateTime.now();
        var existing = codes.findByUserId(user.getId());

        if (existing.isPresent()) {
            var previous = existing.get();
            if (previous.getCreatedAt().plusSeconds(RESEND_SECONDS).isAfter(now)) {
                return;
            }
            if (!previous.isWindowExpired(now)
                    && (previous.getSendCount() >= MAX_SEND_COUNT
                    || previous.getFailedAttempts() >= MAX_FAILED_ATTEMPTS)) {
                return;
            }
        }

        String code;
        do {
            code = String.format(Locale.ROOT, "%06d", random.nextInt(1_000_000));
        } while (existing.isPresent() && passwordEncoder.matches(
                codeMaterial(code, user.getPasswordHash()), existing.get().getCodeHash()));

        // Hash the code together with a stamp of the current password hash.
        // A normal password change also makes pending reset codes unusable.
        String hash = passwordEncoder.encode(codeMaterial(code, user.getPasswordHash()));
        LocalDateTime expiresAt = now.plusMinutes(CODE_VALID_MINUTES);
        PasswordResetCodeEntity entity;
        if (existing.isEmpty()) {
            entity = new PasswordResetCodeEntity(user.getId(), hash, now, expiresAt);
        } else {
            entity = existing.get();
            if (entity.isWindowExpired(now)) {
                entity.startNewWindow(hash, now, expiresAt);
            } else {
                entity.renewCode(hash, now, expiresAt);
            }
        }
        codes.saveAndFlush(entity);

        try {
            emailService.sendEmail(user.getEmail(),
                    "Sports Training Assistant - Password Reset Code",
                    "Your password reset code is: " + code
                            + "\n\nEnter this code on the Forgot Password page."
                            + "\nThe code is valid for up to 10 minutes."
                            + "\nOnly the latest code is valid. Do not share it."
                            + "\nIf you did not request a reset, ignore this email.");
        } catch (MailException ex) {
            // Keep the same public response and preserve rate limiting.
            // Do not log the email address, code, or mail-server credentials.
            log.warn("Password reset email delivery failed: {}", ex.getClass().getSimpleName());
        }
    }

    // ==================== Confirm Reset ====================

    public enum ResetResult {
        SUCCESS, INVALID_OR_EXPIRED, SAME_PASSWORD
    }

    @Transactional
    public ResetResult confirmReset(String email, String code, String newPassword) {
        String normalized = normalizeEmail(email);
        validatePassword(newPassword);
        if (code == null || !code.matches("[0-9]{6}")) {
            return ResetResult.INVALID_OR_EXPIRED;
        }

        var found = users.findByEmailForUpdate(normalized);
        if (found.isEmpty() || !found.get().isEmailVerified()) {
            return ResetResult.INVALID_OR_EXPIRED;
        }
        var user = found.get();
        var existing = codes.findByUserId(user.getId());
        if (existing.isEmpty()) {
            return ResetResult.INVALID_OR_EXPIRED;
        }
        var entity = existing.get();
        LocalDateTime now = LocalDateTime.now();
        if (entity.isWindowExpired(now) || entity.isExpired(now)
                || entity.getFailedAttempts() >= MAX_FAILED_ATTEMPTS) {
            return ResetResult.INVALID_OR_EXPIRED;
        }

        if (!passwordEncoder.matches(codeMaterial(code, user.getPasswordHash()),
                entity.getCodeHash())) {
            entity.recordFailedAttempt();
            codes.save(entity);
            // Return, rather than throw, so the failed attempt is committed.
            return ResetResult.INVALID_OR_EXPIRED;
        }

        if (passwordEncoder.matches(newPassword, user.getPasswordHash())) {
            return ResetResult.SAME_PASSWORD;
        }

        user.changePasswordHash(passwordEncoder.encode(newPassword));
        users.save(user);
        entity.invalidateCode(now);
        codes.save(entity);
        return ResetResult.SUCCESS;
    }

    private static String normalizeEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        return email.trim().toLowerCase(Locale.ROOT);
    }

    private static void validatePassword(String password) {
        if (password == null || password.isBlank() || password.length() < 8
                || password.length() > 72
                || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new IllegalArgumentException(
                    "Password must be 8-72 characters and at most 72 UTF-8 bytes");
        }
    }

    private static String codeMaterial(String code, String passwordHash) {
        // Six digits plus a 64-character SHA-256 stamp = 70 ASCII bytes.
        return code + PasswordCredentialStamp.of(passwordHash);
    }
}
