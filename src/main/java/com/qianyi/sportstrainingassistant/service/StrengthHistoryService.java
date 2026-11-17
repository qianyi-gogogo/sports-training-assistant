
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.entity.*;
import com.qianyi.sportstrainingassistant.model.*;
import com.qianyi.sportstrainingassistant.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StrengthHistoryService {

    private final StrengthHistorySnapshotRepository snapshotRepository;
    private final StrengthHistoryRecordRepository recordRepository;

    public StrengthHistoryService(
            StrengthHistorySnapshotRepository snapshotRepository,
            StrengthHistoryRecordRepository recordRepository
    ) {
        this.snapshotRepository = snapshotRepository;
        this.recordRepository = recordRepository;
    }

    // ==================== Ordinary Exercise ====================

    @Transactional
    public void saveExerciseSnapshot(
            Long bodyProfileId,
            StrengthHistoryExerciseType exerciseType,
            ExerciseStrength exercise
    ) {
        validateProfileId(bodyProfileId);

        if (exerciseType == null ||
                exerciseType == StrengthHistoryExerciseType.PULL_UP) {
            throw new IllegalArgumentException(
                    "Invalid ordinary exercise type"
            );
        }

        if (exercise == null ||
                exercise.getRecords() == null ||
                exercise.getRecords().isEmpty()) {
            throw new IllegalArgumentException(
                    "Exercise records cannot be empty"
            );
        }

        // Create one snapshot header.
        StrengthHistorySnapshotEntity snapshot =
                snapshotRepository.saveAndFlush(
                        new StrengthHistorySnapshotEntity(
                                bodyProfileId,
                                exerciseType
                        )
                );

        List<StrengthHistoryRecordEntity> records =
                new ArrayList<>();

        List<StrengthRecord> source = exercise.getRecords();

        for (int i = 0; i < source.size(); i++) {
            records.add(
                    new StrengthHistoryRecordEntity(
                            snapshot.getId(),
                            i,
                            source.get(i)
                    )
            );
        }

        recordRepository.saveAll(records);
    }

    // ==================== Pull-up ====================

    @Transactional
    public void savePullUpSnapshot(
            Long bodyProfileId,
            PullUpStrength pullUp
    ) {
        validateProfileId(bodyProfileId);

        if (pullUp == null ||
                pullUp.getRecords() == null ||
                pullUp.getRecords().isEmpty()) {
            throw new IllegalArgumentException(
                    "Pull-up records cannot be empty"
            );
        }

        StrengthHistorySnapshotEntity snapshot =
                snapshotRepository.saveAndFlush(
                        new StrengthHistorySnapshotEntity(
                                bodyProfileId,
                                StrengthHistoryExerciseType.PULL_UP
                        )
                );

        List<StrengthHistoryRecordEntity> records =
                new ArrayList<>();

        List<PullUpRecord> source = pullUp.getRecords();

        for (int i = 0; i < source.size(); i++) {
            records.add(
                    new StrengthHistoryRecordEntity(
                            snapshot.getId(),
                            i,
                            source.get(i)
                    )
            );
        }

        recordRepository.saveAll(records);
    }

    // ==================== Validation ====================

    private void validateProfileId(Long bodyProfileId) {

        if (bodyProfileId == null || bodyProfileId <= 0) {
            throw new IllegalArgumentException(
                    "Body profile ID must be positive"
            );
        }
    }
}
