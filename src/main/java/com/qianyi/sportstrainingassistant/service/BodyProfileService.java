
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.BodyProfileEntity;
import com.qianyi.sportstrainingassistant.model.BodyProfile;
import com.qianyi.sportstrainingassistant.repository.BodyProfileRepository;
import com.qianyi.sportstrainingassistant.entity.BodyProfileHistoryEntity;
import com.qianyi.sportstrainingassistant.repository.BodyProfileHistoryRepository;
import com.qianyi.sportstrainingassistant.dto.response.profile.BodyProfileHistoryResponse;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import com.qianyi.sportstrainingassistant.dto.request.profile.BodyProfilePatchRequest;
import com.qianyi.sportstrainingassistant.model.BodyMeasurements;
import java.util.NoSuchElementException;

@Service
public class BodyProfileService {

    private final BodyProfileRepository bodyProfileRepository;
    private final StrengthProfilePersistenceService strengthService;
    private final BodyProfileHistoryRepository historyRepository;
    // ==================== Constructor ====================


    public BodyProfileService(
            BodyProfileRepository bodyProfileRepository,
            StrengthProfilePersistenceService strengthService,
            BodyProfileHistoryRepository historyRepository
    ) {
        this.bodyProfileRepository = bodyProfileRepository;
        this.strengthService = strengthService;
        this.historyRepository = historyRepository;
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
        // Save initial history.
        saveHistory(saved);
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

    // ==================== Partial Update ====================

    @Transactional
    public BodyProfile patchProfile(
            Long userId,
            BodyProfilePatchRequest request
    ) {
        validateUserId(userId);

        if (request == null
                || (request.getWeightKg() == null
                && request.getWaistCircumferenceCm() == null)) {
            throw new IllegalArgumentException(
                    "At least one field must be provided"
            );
        }

        // 1. Find the existing profile.
        BodyProfileEntity entity = bodyProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Body profile not found"
                        )
                );

        // 2. Convert the entity into a Model.
        BodyProfile profile = entity.toModel();

        double oldWeight =
                profile.getBasicBodyInfo().getWeightKg();

        Double oldWaist =
                profile.getBodyMeasurements() == null
                        ? null
                        : profile.getBodyMeasurements()
                        .getWaistCircumferenceCm();

        // 3. Update weight only when provided.
        if (request.getWeightKg() != null) {
            profile.getBasicBodyInfo().setWeightKg(
                    request.getWeightKg()
            );
        }

        // 4. Update waist only when provided.
        if (request.getWaistCircumferenceCm() != null) {

            BodyMeasurements measurements =
                    profile.getBodyMeasurements();

            if (measurements == null) {
                measurements = new BodyMeasurements();
                profile.setBodyMeasurements(measurements);
            }

            measurements.setWaistCircumferenceCm(
                    request.getWaistCircumferenceCm()
            );
        }

        // ==================== Detect Actual Changes ====================

        double newWeight =
                profile.getBasicBodyInfo().getWeightKg();

        Double newWaist =
                profile.getBodyMeasurements() == null
                        ? null
                        : profile.getBodyMeasurements()
                        .getWaistCircumferenceCm();

        boolean changed =
                Double.compare(oldWeight, newWeight) != 0
                        || !Objects.equals(oldWaist, newWaist);

        if (!changed) {
            return buildCompleteProfile(entity);
        }

        // 5. Validate and apply the updated data.
        entity.updateFromModel(profile);

        // 6. Save the updated entity.
        BodyProfileEntity saved =
                bodyProfileRepository.saveAndFlush(entity);

// Record the updated body state.
        saveHistory(saved);

        return buildCompleteProfile(saved);
    }

    // ==================== Save History ====================

    private void saveHistory(BodyProfileEntity entity) {

        BodyProfile profile = entity.toModel();

        Double waist = null;

        if (profile.getBodyMeasurements() != null) {
            waist = profile.getBodyMeasurements()
                    .getWaistCircumferenceCm();
        }

        BodyProfileHistoryEntity history =
                new BodyProfileHistoryEntity(
                        entity.getId(),
                        profile.getBasicBodyInfo().getWeightKg(),
                        waist
                );

        historyRepository.save(history);
    }

    // ==================== Get Profile History ====================

    @Transactional(readOnly = true)
    public List<BodyProfileHistoryResponse> getProfileHistory(
            Long userId
    ) {
        // 1. Validate user ID.
        validateUserId(userId);

        // 2. Find the user's body profile.
        BodyProfileEntity entity = bodyProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Body profile not found"
                        )
                );

        // 3. Load history in chronological order.
        return historyRepository
                .findByBodyProfileIdOrderByRecordedAtAscIdAsc(
                        entity.getId()
                )
                .stream()
                .map(BodyProfileHistoryResponse::fromEntity)
                .toList();
    }



}
