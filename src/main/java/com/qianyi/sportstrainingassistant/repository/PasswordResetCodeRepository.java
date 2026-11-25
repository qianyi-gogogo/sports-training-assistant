package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.PasswordResetCodeEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import java.util.Optional;

public interface PasswordResetCodeRepository
        extends JpaRepository<PasswordResetCodeEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PasswordResetCodeEntity> findByUserId(Long userId);
}
