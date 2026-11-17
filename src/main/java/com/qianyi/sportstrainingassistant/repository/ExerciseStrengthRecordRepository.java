
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.ExerciseStrengthRecordEntity;
import com.qianyi.sportstrainingassistant.entity.StrengthExerciseType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExerciseStrengthRecordRepository
        extends JpaRepository<ExerciseStrengthRecordEntity, Long> {

    // ==================== Read ====================

    List<ExerciseStrengthRecordEntity>
    findByBodyProfileIdAndExerciseTypeOrderByRecordIndexAsc(
            Long bodyProfileId,
            StrengthExerciseType exerciseType
    );

    List<ExerciseStrengthRecordEntity>
    findByBodyProfileIdOrderByExerciseTypeAscRecordIndexAsc(
            Long bodyProfileId
    );

    // ==================== Delete Current Records ====================

    @Modifying(flushAutomatically = true)
    @Query("""
            DELETE FROM ExerciseStrengthRecordEntity r
            WHERE r.bodyProfileId = :bodyProfileId
              AND r.exerciseType = :exerciseType
            """)
    int deleteCurrentExerciseRecords(
            @Param("bodyProfileId") Long bodyProfileId,
            @Param("exerciseType") StrengthExerciseType exerciseType
    );
}
