
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.*;
import com.qianyi.sportstrainingassistant.model.*;

import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ExerciseStrengthRecordRepositoryTest {

    @Autowired
    private ExerciseStrengthRecordRepository repository;

    @Autowired
    private BodyProfileRepository bodyProfileRepository;

    @Autowired
    private EntityManager entityManager;


    // ==================== Test Data ====================

    private Long createBodyProfileId() {

        BasicBodyInfo basic = new BasicBodyInfo(
                175.0,
                70.0,
                21,
                BiologicalSex.MALE
        );

        BodyProfile profile = new BodyProfile(basic);

        Long userId = UUID.randomUUID()
                .getMostSignificantBits() & Long.MAX_VALUE;

        BodyProfileEntity saved =
                bodyProfileRepository.saveAndFlush(
                        new BodyProfileEntity(userId, profile)
                );

        return saved.getId();
    }


    // ==================== Save And Read ====================

    @Test
    void shouldSaveAndRestoreStrengthRecord() {

        Long profileId = createBodyProfileId();

        StrengthRecord record =
                new StrengthRecord(80.0, 5, 1);

        repository.saveAndFlush(
                new ExerciseStrengthRecordEntity(
                        profileId,
                        StrengthExerciseType.BENCH_PRESS,
                        0,
                        record
                )
        );

        entityManager.clear();

        List<ExerciseStrengthRecordEntity> results =
                repository
                        .findByBodyProfileIdAndExerciseTypeOrderByRecordIndexAsc(
                                profileId,
                                StrengthExerciseType.BENCH_PRESS
                        );

        assertEquals(1, results.size());

        StrengthRecord restored = results.get(0).toModel();

        assertEquals(80.0, restored.getWeightKg());
        assertEquals(5, restored.getReps());
        assertEquals(1, restored.getRir());
    }


    // ==================== Record Order ====================

    @Test
    void shouldPreserveStrengthRecordOrder() {

        Long profileId = createBodyProfileId();

        repository.saveAndFlush(
                new ExerciseStrengthRecordEntity(
                        profileId,
                        StrengthExerciseType.BENCH_PRESS,
                        0,
                        new StrengthRecord(80.0, 3, 1)
                )
        );

        repository.saveAndFlush(
                new ExerciseStrengthRecordEntity(
                        profileId,
                        StrengthExerciseType.BENCH_PRESS,
                        1,
                        new StrengthRecord(70.0, 8, 2)
                )
        );

        entityManager.clear();

        List<ExerciseStrengthRecordEntity> results =
                repository
                        .findByBodyProfileIdAndExerciseTypeOrderByRecordIndexAsc(
                                profileId,
                                StrengthExerciseType.BENCH_PRESS
                        );

        assertEquals(2, results.size());

        assertEquals(0, results.get(0).getRecordIndex());
        assertEquals(1, results.get(1).getRecordIndex());

        assertEquals(
                80.0,
                results.get(0).toModel().getWeightKg()
        );

        assertEquals(
                70.0,
                results.get(1).toModel().getWeightKg()
        );
    }
}
