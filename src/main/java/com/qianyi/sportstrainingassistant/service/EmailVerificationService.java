
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.dto.response.auth.EmailVerificationResponse;
import com.qianyi.sportstrainingassistant.entity.EmailVerificationCodeEntity;
import com.qianyi.sportstrainingassistant.entity.UserEntity;
import com.qianyi.sportstrainingassistant.model.EmailVerificationStatus;
import com.qianyi.sportstrainingassistant.repository.EmailVerificationCodeRepository;
import com.qianyi.sportstrainingassistant.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;

@Service
public class EmailVerificationService {

    // ==================== Configuration ====================

    private static final int CODE_VALID_MINUTES = 10;
    private static final int RESEND_SECONDS = 60;
    private static final int MAX_SEND_COUNT = 5;
    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final int WINDOW_HOURS = 1;

    private final SecureRandom random = new SecureRandom();

    private final UserRepository userRepository;
    private final EmailVerificationCodeRepository codeRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    // ==================== Constructor ====================

    public EmailVerificationService(
            UserRepository userRepository,
            EmailVerificationCodeRepository codeRepository,
            EmailService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    // ==================== Send Verification Code ====================

    @Transactional
    public EmailVerificationResponse sendVerificationCode(
            Long userId
    ) {
        var result = userRepository.findByIdForUpdate(
                userId
        );

        if (result.isEmpty()) {
            return unavailableResponse();
        }

        UserEntity user = result.get();

        if (user.isEmailVerified()) {
            return unavailableResponse();
        }

        LocalDateTime now = LocalDateTime.now();

        var existing = codeRepository.findByUserId(
                user.getId()
        );

        // ==================== Check Rate Limits ====================

        if (existing.isPresent()) {

            EmailVerificationCodeEntity previous =
                    existing.get();

            // The resend interval also applies across window boundaries.
            if (previous.getCreatedAt()
                    .plusSeconds(RESEND_SECONDS)
                    .isAfter(now)) {

                return buildResponse(
                        EmailVerificationStatus.RESEND_COOLDOWN,
                        previous,
                        now
                );
            }

            if (!previous.isWindowExpired(now)) {

                // Do not send unusable verification codes.
                if (previous.getFailedAttempts()
                        >= MAX_FAILED_ATTEMPTS) {

                    return buildResponse(
                            EmailVerificationStatus.ATTEMPT_LIMIT_REACHED,
                            previous,
                            now
                    );
                }

                if (previous.getSendCount()
                        >= MAX_SEND_COUNT) {

                    return buildResponse(
                            EmailVerificationStatus.SEND_LIMIT_REACHED,
                            previous,
                            now
                    );
                }
            }
        }

        // ==================== Generate Code ====================

        String code = String.format(
                Locale.ROOT,
                "%06d",
                random.nextInt(1_000_000)
        );

        String codeHash = passwordEncoder.encode(code);

        LocalDateTime expiresAt =
                now.plusMinutes(CODE_VALID_MINUTES);

        EmailVerificationCodeEntity entity;

        // ==================== Save Verification Record ====================

        if (existing.isEmpty()) {

            entity = new EmailVerificationCodeEntity(
                    user.getId(),
                    codeHash,
                    now,
                    expiresAt
            );

        } else {

            entity = existing.get();

            if (entity.isWindowExpired(now)) {

                entity.startNewWindow(
                        codeHash,
                        now,
                        expiresAt
                );

            } else {

                entity.renewCode(
                        codeHash,
                        now,
                        expiresAt
                );
            }
        }

        codeRepository.saveAndFlush(entity);

        // ==================== Send Email ====================

        emailService.sendEmail(
                user.getEmail(),
                "Sports Training Assistant - Email Verification",
                "Your verification code is: " + code
                        + "\n\nThis code expires in 10 minutes."
                        + "\nMaximum 5 failed attempts per verification window."
                        + "\nOnly the latest verification code is valid."
                        + "\nDo not share this code with anyone."
        );

        return buildResponse(
                EmailVerificationStatus.CODE_SENT,
                entity,
                LocalDateTime.now()
        );
    }

    // ==================== Verify Code ====================

    @Transactional
    public EmailVerificationResponse verifyCode(
            Long userId,
            String code
    ) {

        if (code == null || !code.matches("[0-9]{6}")) {
            return unavailableResponse();
        }

        var result = userRepository.findByIdForUpdate(
                userId
        );

        if (result.isEmpty()) {
            return unavailableResponse();
        }

        UserEntity user = result.get();

        if (user.isEmailVerified()) {
            return unavailableResponse();
        }

        var verification = codeRepository.findByUserId(
                user.getId()
        );

        if (verification.isEmpty()) {
            return unavailableResponse();
        }

        EmailVerificationCodeEntity entity =
                verification.get();

        LocalDateTime now = LocalDateTime.now();

        // ==================== Check Verification Window ====================

        if (entity.isWindowExpired(now)) {

            return buildResponse(
                    EmailVerificationStatus.CODE_EXPIRED,
                    entity,
                    now
            );
        }

        // ==================== Check Attempt Limit ====================

        if (entity.getFailedAttempts()
                >= MAX_FAILED_ATTEMPTS) {

            return buildResponse(
                    EmailVerificationStatus.ATTEMPT_LIMIT_REACHED,
                    entity,
                    now
            );
        }

        // ==================== Check Code Expiration ====================

        if (entity.isExpired(now)) {

            return buildResponse(
                    EmailVerificationStatus.CODE_EXPIRED,
                    entity,
                    now
            );
        }

        // ==================== Compare Code ====================

        if (!passwordEncoder.matches(
                code,
                entity.getCodeHash()
        )) {

            entity.recordFailedAttempt();

            codeRepository.save(entity);

            EmailVerificationStatus status =
                    entity.getFailedAttempts()
                            >= MAX_FAILED_ATTEMPTS

                            ? EmailVerificationStatus.ATTEMPT_LIMIT_REACHED

                            : EmailVerificationStatus.INVALID_CODE;

            return buildResponse(
                    status,
                    entity,
                    now
            );
        }

        // ==================== Verification Success ====================

        user.markEmailVerified();

        userRepository.save(user);

        codeRepository.delete(entity);

        return new EmailVerificationResponse(
                EmailVerificationStatus.VERIFIED,
                true,
                null,
                null,
                null,
                null,
                null
        );
    }

    // ==================== Build Response ====================

    private EmailVerificationResponse buildResponse(
            EmailVerificationStatus status,
            EmailVerificationCodeEntity entity,
            LocalDateTime now
    ) {

        boolean windowExpired =
                entity.isWindowExpired(now);

        int remainingSends = windowExpired
                ? MAX_SEND_COUNT
                : Math.max(
                0,
                MAX_SEND_COUNT - entity.getSendCount()
        );

        int remainingAttempts = windowExpired
                ? MAX_FAILED_ATTEMPTS
                : Math.max(
                0,
                MAX_FAILED_ATTEMPTS
                        - entity.getFailedAttempts()
        );

        long windowResetAfter = secondsUntil(
                now,
                entity.getWindowStartedAt()
                        .plusHours(WINDOW_HOURS)
        );

        long resendAfter = secondsUntil(
                now,
                entity.getCreatedAt()
                        .plusSeconds(RESEND_SECONDS)
        );

        long codeExpiresAfter = Math.min(
                secondsUntil(now, entity.getExpiresAt()),
                windowResetAfter
        );

        // Block sending when either hourly limit is reached.
        if (remainingSends == 0
                || remainingAttempts == 0) {

            resendAfter = Math.max(
                    resendAfter,
                    windowResetAfter
            );
        }

        return new EmailVerificationResponse(
                status,
                false,
                remainingSends,
                remainingAttempts,
                resendAfter,
                codeExpiresAfter,
                windowResetAfter
        );
    }

    // ==================== Unavailable Response ====================

    private EmailVerificationResponse unavailableResponse() {

        return new EmailVerificationResponse(
                EmailVerificationStatus.INVALID_OR_UNAVAILABLE,
                false,
                null,
                null,
                null,
                null,
                null
        );
    }

    // ==================== Time Calculation ====================

    private long secondsUntil(
            LocalDateTime now,
            LocalDateTime target
    ) {

        Duration duration = Duration.between(now, target);

        if (duration.isNegative() || duration.isZero()) {
            return 0;
        }

        // Round up to avoid enabling a button too early.
        return duration.getSeconds()
                + (duration.getNano() > 0 ? 1 : 0);
    }

    // ==================== Normalize Email ====================

    private String normalizeEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Email is required"
            );
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
}
