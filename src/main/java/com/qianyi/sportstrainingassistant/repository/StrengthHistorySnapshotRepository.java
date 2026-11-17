
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.StrengthHistorySnapshotEntity;
import com.qianyi.sportstrainingassistant.entity.StrengthHistoryExerciseType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StrengthHistorySnapshotRepository
        extends JpaRepository<StrengthHistorySnapshotEntity, Long> {

    // Find all snapshots for a body profile, newest first.
    List<StrengthHistorySnapshotEntity>
    findByBodyProfileIdOrderByRecordedAtDescIdDesc(
            Long bodyProfileId
    );

    // Find the history of a specific exercise, newest first.
    List<StrengthHistorySnapshotEntity>
    findByBodyProfileIdAndExerciseTypeOrderByRecordedAtDescIdDesc(
            Long bodyProfileId,
            StrengthHistoryExerciseType exerciseType
    );
}
