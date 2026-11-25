
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.UserEntity;
import com.qianyi.sportstrainingassistant.repository.UserRepository;
import com.qianyi.sportstrainingassistant.repository.EmailVerificationCodeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccountCleanupService {

    // ==================== Configuration ====================

    private static final int ACCOUNT_RETENTION_HOURS = 1;

    private final UserRepository userRepository;
    private final EmailVerificationCodeRepository codeRepository;

    // ==================== Constructor ====================

    public AccountCleanupService(
            UserRepository userRepository,
            EmailVerificationCodeRepository codeRepository
    ) {
        this.userRepository = userRepository;
        this.codeRepository = codeRepository;
    }

    // ==================== Account Expiration ====================

    private boolean canDeleteAccount(
            UserEntity user,
            LocalDateTime now
    ) {

        // Never delete verified accounts.
        if (user.isEmailVerified()) {
            return false;
        }

        // Protect legacy accounts without an email.
        if (user.getEmail() == null
                || user.getEmail().isBlank()) {
            return false;
        }

        // Check the minimum account retention period.
        if (user.getCreatedAt() == null
                || user.getCreatedAt()
                .plusHours(ACCOUNT_RETENTION_HOURS)
                .isAfter(now)) {

            return false;
        }

        // Protect an active verification rate-limit window.
        var verification = codeRepository.findByUserId(
                user.getId()
        );

        if (verification.isPresent()) {

            var code = verification.get();

            if (!code.isWindowExpired(now)) {
                return false;
            }

            // Preserve the resend cooldown across window boundaries.
            if (code.getCreatedAt()
                    .plusSeconds(60)
                    .isAfter(now)) {
                return false;
            }
        }

        // Protect existing business data.
        if (userRepository.countBodyProfiles(
                user.getId()) > 0) {

            return false;
        }

        if (userRepository.countTrainingGoals(
                user.getId()) > 0) {

            return false;
        }

        return true;
    }

    // ==================== Delete Expired Account ====================

    @Transactional
    public boolean deleteExpiredAccount(Long userId) {

        // Lock the account before checking its current state.
        var result = userRepository.findByIdForUpdate(
                userId
        );

        if (result.isEmpty()) {
            return false;
        }

        UserEntity user = result.get();

        if (!canDeleteAccount(
                user,
                LocalDateTime.now()
        )) {
            return false;
        }

        // Related verification records are removed by ON DELETE CASCADE.
        userRepository.delete(user);

        userRepository.flush();

        return true;
    }

    // ==================== Find Cleanup Candidates ====================

    @Transactional(readOnly = true)
    public List<Long> findExpiredCandidateIds() {

        LocalDateTime cutoff =
                LocalDateTime.now()
                        .minusHours(ACCOUNT_RETENTION_HOURS);

        return userRepository
                .findExpiredUnverifiedCandidateIds(cutoff);
    }
}
