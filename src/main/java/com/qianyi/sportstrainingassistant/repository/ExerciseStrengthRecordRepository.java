
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.ExerciseStrengthRecordEntity;
import com.qianyi.sportstrainingassistant.entity.StrengthExerciseType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseStrengthRecordRepository
        extends JpaRepository<ExerciseStrengthRecordEntity, Long> {

    // Find records for one exercise in their original order.
    List<ExerciseStrengthRecordEntity>
    findByBodyProfileIdAndExerciseTypeOrderByRecordIndexAsc(
            Long bodyProfileId,
            StrengthExerciseType exerciseType
    );

    // Find all ordinary strength records for a body profile.
    List<ExerciseStrengthRecordEntity>
    findByBodyProfileIdOrderByExerciseTypeAscRecordIndexAsc(
            Long bodyProfileId
    );
}
