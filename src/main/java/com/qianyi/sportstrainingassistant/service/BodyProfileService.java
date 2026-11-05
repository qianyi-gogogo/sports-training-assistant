
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

    public BodyProfileService(
            BodyProfileRepository bodyProfileRepository
    ) {
        this.bodyProfileRepository = bodyProfileRepository;
    }


    // ==================== Create ====================

    @Transactional
    public BodyProfile createProfile(
            Long userId,
            BodyProfile profile
    ) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be positive"
            );
        }

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

        BodyProfileEntity entity =
                new BodyProfileEntity(userId, profile);

        BodyProfileEntity saved =
                bodyProfileRepository.saveAndFlush(entity);

        return saved.toModel();
    }


    // ==================== Read ====================

    @Transactional(readOnly = true)
    public Optional<BodyProfile> getCurrentProfile(Long userId) {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be positive"
            );
        }

        return bodyProfileRepository
                .findByUserId(userId)
                .map(BodyProfileEntity::toModel);
    }
}
