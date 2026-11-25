package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.PasswordResetTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetTokenEntity, Long> {

    Optional<PasswordResetTokenEntity> findByUserId(Long userId);
    Optional<PasswordResetTokenEntity> findByTokenHash(String tokenHash);
}
