
package com.qianyi.sportstrainingassistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "email_verification_codes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_verification_user",
                        columnNames = "user_id"
                )
        }
)
public class EmailVerificationCodeEntity {

    // ==================== Identity ====================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // ==================== Verification Code ====================

    @Column(name = "code_hash", nullable = false, length = 100)
    private String codeHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // ==================== Rate Limiting ====================

    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts;

    @Column(name = "send_count", nullable = false)
    private int sendCount;

    @Column(name = "window_started_at", nullable = false)
    private LocalDateTime windowStartedAt;

    // ==================== Constructors ====================

    protected EmailVerificationCodeEntity() {
        // Required by JPA.
    }

    public EmailVerificationCodeEntity(
            Long userId,
            String codeHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt
    ) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(
                    "Invalid user ID"
            );
        }

        this.userId = userId;

        startNewWindow(
                codeHash,
                createdAt,
                expiresAt
        );
    }

    // ==================== Start New Window ====================

    public void startNewWindow(
            String codeHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt
    ) {
        validateCode(codeHash, createdAt, expiresAt);

        this.codeHash = codeHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;

        this.windowStartedAt = createdAt;
        this.sendCount = 1;
        this.failedAttempts = 0;
    }

    // ==================== Renew Code ====================

    public void renewCode(
            String codeHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt
    ) {
        validateCode(codeHash, createdAt, expiresAt);

        this.codeHash = codeHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;

        // Count the new email without resetting failed attempts.
        this.sendCount++;
    }

    // ==================== Failed Attempts ====================

    public void recordFailedAttempt() {
        this.failedAttempts++;
    }

    // ==================== Time Validation ====================

    public boolean isExpired(LocalDateTime now) {
        return !expiresAt.isAfter(now);
    }

    public boolean isWindowExpired(LocalDateTime now) {
        return !windowStartedAt
                .plusHours(1)
                .isAfter(now);
    }

    // ==================== Code Validation ====================

    private void validateCode(
            String codeHash,
            LocalDateTime createdAt,
            LocalDateTime expiresAt
    ) {
        if (codeHash == null
                || codeHash.isBlank()
                || codeHash.length() > 100) {

            throw new IllegalArgumentException(
                    "Invalid code hash"
            );
        }

        if (createdAt == null
                || expiresAt == null
                || !expiresAt.isAfter(createdAt)) {

            throw new IllegalArgumentException(
                    "Invalid expiration time"
            );
        }
    }

    // ==================== Getters ====================

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCodeHash() {
        return codeHash;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public int getSendCount() {
        return sendCount;
    }

    public LocalDateTime getWindowStartedAt() {
        return windowStartedAt;
    }
}
