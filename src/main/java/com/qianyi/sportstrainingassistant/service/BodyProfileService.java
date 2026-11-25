
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.BodyProfileEntity;
import com.qianyi.sportstrainingassistant.model.BodyProfile;
import com.qianyi.sportstrainingassistant.repository.BodyProfileRepository;
import com.qianyi.sportstrainingassistant.entity.BodyProfileHistoryEntity;
import com.qianyi.sportstrainingassistant.repository.BodyProfileHistoryRepository;
import com.qianyi.sportstrainingassistant.dto.response.profile.BodyProfileHistoryResponse;
import com.qianyi.sportstrainingassistant.dto.request.profile.StrengthProfilePatchRequest;
import com.qianyi.sportstrainingassistant.entity.StrengthExerciseType;
import com.qianyi.sportstrainingassistant.model.StrengthProfile;
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
                || strength.getDeadlift() != null;
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

        if (request == null || !request.isUpdateProvided()) {
            throw new IllegalArgumentException(
                    "At least one field must be provided"
            );
        }

        // ==================== Find Profile ====================

        BodyProfileEntity entity = bodyProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Body profile not found"
                        )
                );

        BodyProfile profile = entity.toModel();

        // ==================== Original Values ====================

        var basic = profile.getBasicBodyInfo();

        BodyMeasurements measurements =
                profile.getBodyMeasurements();

        if (measurements == null) {
            measurements = new BodyMeasurements();
            profile.setBodyMeasurements(measurements);
        }

        double oldWeight = basic.getWeightKg();
        Double oldBodyFat = basic.getBodyFatPercentage();

        Double oldChest =
                measurements.getChestCircumferenceCm();

        Double oldWaist =
                measurements.getWaistCircumferenceCm();

        Double oldLeftArm =
                measurements.getLeftArmCircumferenceCm();

        Double oldRightArm =
                measurements.getRightArmCircumferenceCm();

        Double oldThigh =
                measurements.getThighCircumferenceCm();

        // ==================== Apply Updates ====================

        // Weight
        if (request.getWeightKg() != null) {
            basic.setWeightKg(request.getWeightKg());
        }

        // Body Fat
        if (request.getBodyFatPercentage() != null) {
            basic.setBodyFatPercentage(
                    request.getBodyFatPercentage()
            );
        }

        // Chest
        if (request.getChestCircumferenceCm() != null) {
            measurements.setChestCircumferenceCm(
                    request.getChestCircumferenceCm()
            );
        }

        // Waist
        if (request.getWaistCircumferenceCm() != null) {
            measurements.setWaistCircumferenceCm(
                    request.getWaistCircumferenceCm()
            );
        }

        // Left Arm
        if (request.getLeftArmCircumferenceCm() != null) {
            measurements.setLeftArmCircumferenceCm(
                    request.getLeftArmCircumferenceCm()
            );
        }

        // Right Arm
        if (request.getRightArmCircumferenceCm() != null) {
            measurements.setRightArmCircumferenceCm(
                    request.getRightArmCircumferenceCm()
            );
        }

        // Thigh
        if (request.getThighCircumferenceCm() != null) {
            measurements.setThighCircumferenceCm(
                    request.getThighCircumferenceCm()
            );
        }

        // ==================== Detect Changes ====================

        boolean changed =
                Double.compare(
                        oldWeight,
                        basic.getWeightKg()
                ) != 0

                        || !Objects.equals(
                        oldBodyFat,
                        basic.getBodyFatPercentage()
                )

                        || !Objects.equals(
                        oldChest,
                        measurements.getChestCircumferenceCm()
                )

                        || !Objects.equals(
                        oldWaist,
                        measurements.getWaistCircumferenceCm()
                )

                        || !Objects.equals(
                        oldLeftArm,
                        measurements.getLeftArmCircumferenceCm()
                )

                        || !Objects.equals(
                        oldRightArm,
                        measurements.getRightArmCircumferenceCm()
                )

                        || !Objects.equals(
                        oldThigh,
                        measurements.getThighCircumferenceCm()
                );

        // No actual change: preserve existing data and history.
        if (!changed) {
            return buildCompleteProfile(entity);
        }

        // ==================== Save Changes ====================

        // Apply validated Model values to Entity.
        entity.updateFromModel(profile);

        BodyProfileEntity saved =
                bodyProfileRepository.saveAndFlush(entity);

        // Append a new historical snapshot.
        saveHistory(saved);

        return buildCompleteProfile(saved);
    }


    // ==================== Save History ====================


    // ==================== Save History ====================

    private void saveHistory(BodyProfileEntity entity) {

        BodyProfile profile = entity.toModel();

        BodyProfileHistoryEntity history =
                new BodyProfileHistoryEntity(
                        entity.getId(),
                        profile
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

    // ==================== PATCH Strength Profile ====================

    @Transactional
    public BodyProfile patchStrengthProfile(
            Long userId,
            StrengthProfilePatchRequest request
    ) {
        validateUserId(userId);

        if (request == null || !request.isUpdateProvided()) {
            throw new IllegalArgumentException(
                    "At least one strength exercise must be provided"
            );
        }

        // Find the user's existing body profile.
        BodyProfileEntity entity = bodyProfileRepository
                .findByUserId(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Body profile not found"
                        )
                );

        Long bodyProfileId = entity.getId();

        // Convert and validate the complete PATCH payload
        // before changing any persistent records.
        StrengthProfile strength =
                BodyProfileRequestMapper.mapStrength(request);

        // ==================== Ordinary Exercises ====================

        strengthService.updateExerciseRecords(
                bodyProfileId,
                StrengthExerciseType.BENCH_PRESS,
                strength.getBenchPress()
        );

        strengthService.updateExerciseRecords(
                bodyProfileId,
                StrengthExerciseType.BARBELL_ROW,
                strength.getBarbellRow()
        );

        strengthService.updateExerciseRecords(
                bodyProfileId,
                StrengthExerciseType.STRICT_PRESS,
                strength.getStrictPress()
        );

        strengthService.updateExerciseRecords(
                bodyProfileId,
                StrengthExerciseType.DUMBBELL_SHOULDER_PRESS,
                strength.getDumbbellShoulderPress()
        );

        strengthService.updateExerciseRecords(
                bodyProfileId,
                StrengthExerciseType.SQUAT,
                strength.getSquat()
        );

        strengthService.updateExerciseRecords(
                bodyProfileId,
                StrengthExerciseType.DEADLIFT,
                strength.getDeadlift()
        );

        // ==================== Pull-Up ====================

        strengthService.updatePullUpRecords(
                bodyProfileId,
                strength.getPullUp()
        );

        // Return the updated complete profile.
        return buildCompleteProfile(entity);
    }



}
