
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.BodyProfileEntity;
import com.qianyi.sportstrainingassistant.model.BodyProfile;
import com.qianyi.sportstrainingassistant.repository.BodyProfileRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class BodyProfileService {

    private final BodyProfileRepository bodyProfileRepository;
    private final StrengthProfilePersistenceService strengthService;

    // ==================== Constructor ====================

    public BodyProfileService(
            BodyProfileRepository bodyProfileRepository,
            StrengthProfilePersistenceService strengthService
    ) {
        this.bodyProfileRepository = bodyProfileRepository;
        this.strengthService = strengthService;
    }

    // ==================== Create ====================

    @Transactional
    public BodyProfile createProfile(
            Long userId,
            BodyProfile profile
    ) {

        validateUserId(userId);

        if (profile == null) {
            throw new IllegalArgumentException(
                    "Body profile cannot be null"
            );
        }

        if (bodyProfileRepository.existsByUserId(userId)) {
            throw new IllegalStateException(
                    "Body profile already exists for this user"
            );
        }

        // 1. Save basic body information.
        BodyProfileEntity entity =
                new BodyProfileEntity(userId, profile);

        BodyProfileEntity saved =
                bodyProfileRepository.saveAndFlush(entity);

        // 2. Save optional strength records.
        strengthService.saveInitialStrength(
                saved.getId(),
                profile.getStrengthProfile()
        );

        // 3. Reconstruct the complete profile.
        return buildCompleteProfile(saved);
    }

    // ==================== Read ====================

    @Transactional(readOnly = true)
    public Optional<BodyProfile> getCurrentProfile(Long userId) {

        validateUserId(userId);

        return bodyProfileRepository
                .findByUserId(userId)
                .map(this::buildCompleteProfile);
    }

    // ==================== Mapping ====================

    private BodyProfile buildCompleteProfile(
            BodyProfileEntity entity
    ) {

        BodyProfile profile = entity.toModel();

        var strength = strengthService.loadStrength(
                entity.getId()
        );

        if (hasStrengthData(strength)) {
            profile.setStrengthProfile(strength);
        }

        return profile;
    }

    private boolean hasStrengthData(
            com.qianyi.sportstrainingassistant.model.StrengthProfile strength
    ) {
        return strength.getBenchPress() != null
                || strength.getBarbellRow() != null
                || strength.getPullUp() != null
                || strength.getStrictPress() != null
                || strength.getDumbbellShoulderPress() != null
                || strength.getSquat() != null
                || strength.getLegPress() != null;
    }

    // ==================== Validation ====================

    private void validateUserId(Long userId) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be positive"
            );
        }
    }
}
