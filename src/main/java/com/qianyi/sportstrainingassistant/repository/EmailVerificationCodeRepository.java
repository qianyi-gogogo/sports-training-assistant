
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.EmailVerificationCodeEntity;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface EmailVerificationCodeRepository
        extends JpaRepository<EmailVerificationCodeEntity, Long> {

    // Find and lock the verification record during an update.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<EmailVerificationCodeEntity> findByUserId(Long userId);
}
