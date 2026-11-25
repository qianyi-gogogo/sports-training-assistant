package com.qianyi.sportstrainingassistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_codes",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_password_reset_user", columnNames = "user_id"))
public class PasswordResetCodeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "code_hash", nullable = false, length = 100)
    private String codeHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "failed_attempts", nullable = false)
    private int failedAttempts;

    @Column(name = "send_count", nullable = false)
    private int sendCount;

    @Column(name = "window_started_at", nullable = false)
    private LocalDateTime windowStartedAt;

    protected PasswordResetCodeEntity() { }

    public PasswordResetCodeEntity(Long userId, String codeHash,
                                   LocalDateTime createdAt, LocalDateTime expiresAt) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }
        this.userId = userId;
        startNewWindow(codeHash, createdAt, expiresAt);
    }

    public void startNewWindow(String codeHash, LocalDateTime createdAt,
                               LocalDateTime expiresAt) {
        setCode(codeHash, createdAt, expiresAt);
        this.windowStartedAt = createdAt;
        this.sendCount = 1;
        this.failedAttempts = 0;
    }

    public void renewCode(String codeHash, LocalDateTime createdAt,
                          LocalDateTime expiresAt) {
        setCode(codeHash, createdAt, expiresAt);
        // Resending a code must not reset the failed-attempt counter.
        this.sendCount++;
    }

    private void setCode(String codeHash, LocalDateTime createdAt,
                         LocalDateTime expiresAt) {
        if (codeHash == null || codeHash.isBlank() || codeHash.length() > 100) {
            throw new IllegalArgumentException("Invalid code hash");
        }
        if (createdAt == null || expiresAt == null || !expiresAt.isAfter(createdAt)) {
            throw new IllegalArgumentException("Invalid code expiration time");
        }
        this.codeHash = codeHash;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public void recordFailedAttempt() {
        this.failedAttempts++;
    }

    public void invalidateCode(LocalDateTime now) {
        // Keep the rate-limit window after a successful password reset.
        this.expiresAt = now;
    }

    public boolean isExpired(LocalDateTime now) {
        return !expiresAt.isAfter(now);
    }

    public boolean isWindowExpired(LocalDateTime now) {
        return !windowStartedAt.plusHours(1).isAfter(now);
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getCodeHash() { return codeHash; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public int getFailedAttempts() { return failedAttempts; }
    public int getSendCount() { return sendCount; }
    public LocalDateTime getWindowStartedAt() { return windowStartedAt; }
}
