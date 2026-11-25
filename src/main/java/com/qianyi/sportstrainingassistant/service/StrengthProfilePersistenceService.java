
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
    private final StrengthHistoryService historyService;
    private final StrengthHistorySnapshotRepository historySnapshotRepository;


    public StrengthProfilePersistenceService(
            ExerciseStrengthRecordRepository exerciseRepository,
            PullUpRecordRepository pullUpRepository,
            StrengthHistoryService historyService,
            StrengthHistorySnapshotRepository historySnapshotRepository
    ) {
        this.exerciseRepository = exerciseRepository;
        this.pullUpRepository = pullUpRepository;
        this.historyService = historyService;
        this.historySnapshotRepository = historySnapshotRepository;
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
                StrengthExerciseType.DEADLIFT,
                strength.getDeadlift());

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

    // ==================== Update Exercise ====================

    @Transactional
    public boolean updateExerciseRecords(
            Long bodyProfileId,
            StrengthExerciseType type,
            ExerciseStrength updatedExercise
    ) {
        validateBodyProfileId(bodyProfileId);

        if (type == null) {
            throw new IllegalArgumentException(
                    "Exercise type cannot be null"
            );
        }

        // Null means no change.
        if (updatedExercise == null) {
            return false;
        }

        // Read current strength records.
        ExerciseStrength currentExercise =
                loadExercise(bodyProfileId, type);

        // Skip identical records.
        if (sameRecords(currentExercise, updatedExercise)) {
            return false;
        }

        StrengthHistoryExerciseType historyType =
                StrengthHistoryExerciseType.valueOf(type.name());

        // Preserve existing records if no history exists yet.
        if (currentExercise != null &&
                historySnapshotRepository
                        .findByBodyProfileIdAndExerciseTypeOrderByRecordedAtDescIdDesc(
                                bodyProfileId,
                                historyType
                        ).isEmpty()) {

            historyService.saveExerciseSnapshot(
                    bodyProfileId,
                    historyType,
                    currentExercise
            );
        }

        // Replace only this exercise's current records.
        exerciseRepository.deleteCurrentExerciseRecords(
                bodyProfileId,
                type
        );

        saveExercise(
                bodyProfileId,
                type,
                updatedExercise
        );

        exerciseRepository.flush();

        // Save the new snapshot.
        historyService.saveExerciseSnapshot(
                bodyProfileId,
                historyType,
                updatedExercise
        );

        return true;
    }



    // ==================== Compare Records ====================

    private boolean sameRecords(
            ExerciseStrength first,
            ExerciseStrength second
    ) {
        if (first == null || second == null) {
            return first == second;
        }

        List<StrengthRecord> firstRecords = first.getRecords();
        List<StrengthRecord> secondRecords = second.getRecords();

        if (firstRecords.size() != secondRecords.size()) {
            return false;
        }

        for (int i = 0; i < firstRecords.size(); i++) {

            StrengthRecord a = firstRecords.get(i);
            StrengthRecord b = secondRecords.get(i);

            if (Double.compare(
                    a.getWeightKg(),
                    b.getWeightKg()
            ) != 0) {
                return false;
            }

            if (a.getReps() != b.getReps()) {
                return false;
            }

            if (!java.util.Objects.equals(
                    a.getRir(),
                    b.getRir()
            )) {
                return false;
            }
        }

        return true;
    }


    // ==================== Update Pull-Up ====================

    @Transactional
    public boolean updatePullUpRecords(
            Long bodyProfileId,
            PullUpStrength updatedPullUp
    ) {
        validateBodyProfileId(bodyProfileId);

        // Null means no change.
        if (updatedPullUp == null) {
            return false;
        }

        // Load current pull-up records.
        List<PullUpRecord> currentRecords = pullUpRepository
                .findByBodyProfileIdOrderByRecordIndexAsc(bodyProfileId)
                .stream()
                .map(PullUpRecordEntity::toModel)
                .toList();

        PullUpStrength currentPullUp =
                currentRecords.isEmpty()
                        ? null
                        : new PullUpStrength(currentRecords);

        // Skip identical records.
        if (samePullUpRecords(currentPullUp, updatedPullUp)) {
            return false;
        }

        // Preserve the original records if no history exists.
        if (currentPullUp != null &&
                historySnapshotRepository
                        .findByBodyProfileIdAndExerciseTypeOrderByRecordedAtDescIdDesc(
                                bodyProfileId,
                                StrengthHistoryExerciseType.PULL_UP
                        ).isEmpty()) {

            historyService.savePullUpSnapshot(
                    bodyProfileId,
                    currentPullUp
            );
        }

        // Replace current pull-up records.
        pullUpRepository.deleteCurrentPullUpRecords(
                bodyProfileId
        );

        savePullUps(
                bodyProfileId,
                updatedPullUp
        );

        pullUpRepository.flush();

        // Save the new history snapshot.
        historyService.savePullUpSnapshot(
                bodyProfileId,
                updatedPullUp
        );

        return true;
    }


    // ==================== Compare Pull-Up Records ====================

    private boolean samePullUpRecords(
            PullUpStrength first,
            PullUpStrength second
    ) {
        if (first == null || second == null) {
            return first == second;
        }

        List<PullUpRecord> firstRecords = first.getRecords();
        List<PullUpRecord> secondRecords = second.getRecords();

        if (firstRecords.size() != secondRecords.size()) {
            return false;
        }

        for (int i = 0; i < firstRecords.size(); i++) {

            PullUpRecord a = firstRecords.get(i);
            PullUpRecord b = secondRecords.get(i);

            if (a.getMode() != b.getMode()) {
                return false;
            }

            if (Double.compare(
                    a.getWeightKg(),
                    b.getWeightKg()
            ) != 0) {
                return false;
            }

            if (a.getReps() != b.getReps()) {
                return false;
            }

            if (!java.util.Objects.equals(
                    a.getRir(),
                    b.getRir()
            )) {
                return false;
            }
        }

        return true;
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

        profile.setDeadlift(loadExercise(
                bodyProfileId, StrengthExerciseType.DEADLIFT));

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

