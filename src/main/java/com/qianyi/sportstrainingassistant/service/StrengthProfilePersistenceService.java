
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.*;
import com.qianyi.sportstrainingassistant.model.*;
import com.qianyi.sportstrainingassistant.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StrengthProfilePersistenceService {

    private final ExerciseStrengthRecordRepository exerciseRepository;
    private final PullUpRecordRepository pullUpRepository;

    public StrengthProfilePersistenceService(
            ExerciseStrengthRecordRepository exerciseRepository,
            PullUpRecordRepository pullUpRepository
    ) {
        this.exerciseRepository = exerciseRepository;
        this.pullUpRepository = pullUpRepository;
    }

    // ==================== Save ====================

    @Transactional
    public void saveInitialStrength(
            Long bodyProfileId,
            StrengthProfile strength
    ) {
        validateBodyProfileId(bodyProfileId);

        if (strength == null) {
            return;
        }

        // Intended only for a newly created body profile.
        // Prevent accidentally inserting duplicate records.
        if (!exerciseRepository
                .findByBodyProfileIdOrderByExerciseTypeAscRecordIndexAsc(
                        bodyProfileId).isEmpty()
                || !pullUpRepository
                .findByBodyProfileIdOrderByRecordIndexAsc(
                        bodyProfileId).isEmpty()) {
            throw new IllegalStateException(
                    "Strength records already exist"
            );
        }

        saveExercise(bodyProfileId,
                StrengthExerciseType.BENCH_PRESS,
                strength.getBenchPress());

        saveExercise(bodyProfileId,
                StrengthExerciseType.BARBELL_ROW,
                strength.getBarbellRow());

        saveExercise(bodyProfileId,
                StrengthExerciseType.STRICT_PRESS,
                strength.getStrictPress());

        saveExercise(bodyProfileId,
                StrengthExerciseType.DUMBBELL_SHOULDER_PRESS,
                strength.getDumbbellShoulderPress());

        saveExercise(bodyProfileId,
                StrengthExerciseType.SQUAT,
                strength.getSquat());

        saveExercise(bodyProfileId,
                StrengthExerciseType.LEG_PRESS,
                strength.getLegPress());

        savePullUps(bodyProfileId, strength.getPullUp());
    }

    private void saveExercise(
            Long bodyProfileId,
            StrengthExerciseType type,
            ExerciseStrength exercise
    ) {
        if (exercise == null) {
            return;
        }

        List<ExerciseStrengthRecordEntity> entities =
                new ArrayList<>();

        List<StrengthRecord> records = exercise.getRecords();

        for (int i = 0; i < records.size(); i++) {
            entities.add(new ExerciseStrengthRecordEntity(
                    bodyProfileId,
                    type,
                    i,
                    records.get(i)
            ));
        }

        exerciseRepository.saveAll(entities);
    }

    private void savePullUps(
            Long bodyProfileId,
            PullUpStrength pullUp
    ) {
        if (pullUp == null) {
            return;
        }

        List<PullUpRecordEntity> entities = new ArrayList<>();
        List<PullUpRecord> records = pullUp.getRecords();

        for (int i = 0; i < records.size(); i++) {
            entities.add(new PullUpRecordEntity(
                    bodyProfileId,
                    i,
                    records.get(i)
            ));
        }

        pullUpRepository.saveAll(entities);
    }

    // ==================== Read ====================

    @Transactional(readOnly = true)
    public StrengthProfile loadStrength(Long bodyProfileId) {
        validateBodyProfileId(bodyProfileId);

        StrengthProfile profile = new StrengthProfile();

        profile.setBenchPress(loadExercise(
                bodyProfileId, StrengthExerciseType.BENCH_PRESS));

        profile.setBarbellRow(loadExercise(
                bodyProfileId, StrengthExerciseType.BARBELL_ROW));

        profile.setStrictPress(loadExercise(
                bodyProfileId, StrengthExerciseType.STRICT_PRESS));

        profile.setDumbbellShoulderPress(loadExercise(
                bodyProfileId,
                StrengthExerciseType.DUMBBELL_SHOULDER_PRESS));

        profile.setSquat(loadExercise(
                bodyProfileId, StrengthExerciseType.SQUAT));

        profile.setLegPress(loadExercise(
                bodyProfileId, StrengthExerciseType.LEG_PRESS));

        List<PullUpRecord> pullUpRecords = pullUpRepository
                .findByBodyProfileIdOrderByRecordIndexAsc(bodyProfileId)
                .stream()
                .map(PullUpRecordEntity::toModel)
                .toList();

        if (!pullUpRecords.isEmpty()) {
            profile.setPullUp(new PullUpStrength(pullUpRecords));
        }

        return profile;
    }

    private ExerciseStrength loadExercise(
            Long bodyProfileId,
            StrengthExerciseType type
    ) {
        List<StrengthRecord> records = exerciseRepository
                .findByBodyProfileIdAndExerciseTypeOrderByRecordIndexAsc(
                        bodyProfileId, type)
                .stream()
                .map(ExerciseStrengthRecordEntity::toModel)
                .toList();

        if (records.isEmpty()) {
            return null;
        }

        return new ExerciseStrength(records);
    }

    // ==================== Validation ====================

    private void validateBodyProfileId(Long bodyProfileId) {
        if (bodyProfileId == null || bodyProfileId <= 0) {
            throw new IllegalArgumentException(
                    "Body profile ID must be positive"
            );
        }
    }
}

