package com.qianyi.sportstrainingassistant.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_reset_user", columnNames = "user_id"),
                @UniqueConstraint(name = "uk_reset_hash", columnNames = "token_hash")
        })
public class PasswordResetTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    // Only store a SHA-256 digest, never the reset token itself.
    @Column(name = "token_hash", nullable = false, length = 64)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    protected PasswordResetTokenEntity() { }

    public PasswordResetTokenEntity(
            Long userId, String tokenHash,
            LocalDateTime expiresAt, LocalDateTime requestedAt
    ) {
        this.userId = userId;
        rotate(tokenHash, expiresAt, requestedAt);
    }

    public void rotate(
            String tokenHash, LocalDateTime expiresAt,
            LocalDateTime requestedAt
    ) {
        if (tokenHash == null || !tokenHash.matches("[0-9a-f]{64}")
                || expiresAt == null || requestedAt == null
                || !expiresAt.isAfter(requestedAt)) {
            throw new IllegalArgumentException("Invalid password reset token");
        }
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.requestedAt = requestedAt;
    }

    public Long getUserId() { return userId; }
    public String getTokenHash() { return tokenHash; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public LocalDateTime getRequestedAt() { return requestedAt; }
}
