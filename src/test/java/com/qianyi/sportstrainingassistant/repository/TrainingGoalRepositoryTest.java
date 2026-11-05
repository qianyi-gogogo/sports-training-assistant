
package com.qianyi.sportstrainingassistant.repository;

import com.qianyi.sportstrainingassistant.entity.TrainingGoalEntity;
import com.qianyi.sportstrainingassistant.model.TrainingGoal;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.qianyi.sportstrainingassistant.model.TrainingGoalType.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TrainingGoalRepositoryTest {

    @Autowired
    private TrainingGoalRepository repository;

    @Autowired
    private EntityManager entityManager;

    private Long createTestUserId() {
        return ThreadLocalRandom.current()
                .nextLong(1, Long.MAX_VALUE);
    }

    private TrainingGoal createGoal() {
        return new TrainingGoal(
                List.of(
                        MUSCLE_GAIN,
                        STRENGTH,
                        FAT_LOSS,
                        ENDURANCE,
                        SPORT_PERFORMANCE
                ),
                "Improve chest development"
        );
    }

    @Test
    void shouldSaveAndFindTrainingGoal() {
        Long userId = createTestUserId();

        TrainingGoalEntity entity =
                new TrainingGoalEntity(userId, createGoal());

        repository.saveAndFlush(entity);
        entityManager.clear();

        TrainingGoalEntity saved = repository
                .findByUserId(userId)
                .orElseThrow();

        assertNotNull(saved.getId());
        assertEquals(userId, saved.getUserId());

        assertEquals(
                createGoal().getGoalPriorities(),
                saved.getGoalPriorities()
        );

        assertEquals(
                "Improve chest development",
                saved.getSpecificGoalDetails()
        );
    }


    @Test
    void shouldPreserveOldVersionWhenSavingNewVersion() {
        Long userId = createTestUserId();

        TrainingGoal originalGoal = createGoal();

        repository.saveAndFlush(
                new TrainingGoalEntity(
                        userId, 1, originalGoal
                )
        );

        TrainingGoal updatedGoal = new TrainingGoal(
                List.of(
                        STRENGTH,
                        MUSCLE_GAIN,
                        ENDURANCE,
                        FAT_LOSS,
                        SPORT_PERFORMANCE
                ),
                "Improve bench press"
        );

        repository.saveAndFlush(
                new TrainingGoalEntity(
                        userId, 2, updatedGoal
                )
        );

        entityManager.clear();

        List<TrainingGoalEntity> history =
                repository.findByUserIdOrderByVersionDesc(userId);

        assertEquals(2, history.size());

        // Verify the newest version.
        assertEquals(2, history.get(0).getVersion());
        assertEquals(
                STRENGTH,
                history.get(0).getGoalPriorities().get(0)
        );

        // Verify the original version is preserved.
        assertEquals(1, history.get(1).getVersion());
        assertEquals(
                MUSCLE_GAIN,
                history.get(1).getGoalPriorities().get(0)
        );

        assertEquals(
                originalGoal.getSpecificGoalDetails(),
                history.get(1).getSpecificGoalDetails()
        );
    }


    @Test
    void shouldFindExistingGoalByUserId() {
        Long userId = createTestUserId();

        repository.saveAndFlush(
                new TrainingGoalEntity(userId, createGoal())
        );

        assertTrue(repository.existsByUserId(userId));
    }

    @Test
    void shouldReturnEmptyForUnknownUser() {
        Long userId = createTestUserId();

        assertTrue(repository.findByUserId(userId).isEmpty());
        assertFalse(repository.existsByUserId(userId));
    }

    @Test
    void shouldKeepOldGoalWhenCreatingNewVersion() {
        Long userId = createTestUserId();

        TrainingGoal firstGoal = createGoal();

        TrainingGoal secondGoal = new TrainingGoal(
                List.of(
                        STRENGTH,
                        MUSCLE_GAIN,
                        ENDURANCE,
                        FAT_LOSS,
                        SPORT_PERFORMANCE
                ),
                "Improve maximum strength"
        );

        repository.saveAndFlush(
                new TrainingGoalEntity(userId, 1, firstGoal)
        );

        repository.saveAndFlush(
                new TrainingGoalEntity(userId, 2, secondGoal)
        );

        entityManager.clear();

        List<TrainingGoalEntity> history =
                repository.findByUserIdOrderByVersionDesc(userId);

        assertEquals(2, history.size());

        assertEquals(2, history.get(0).getVersion());
        assertEquals(1, history.get(1).getVersion());

        assertEquals(
                secondGoal.getGoalPriorities(),
                history.get(0).getGoalPriorities()
        );

        assertEquals(
                firstGoal.getGoalPriorities(),
                history.get(1).getGoalPriorities()
        );
    }

    @Test
    void shouldFindLatestGoalVersion() {
        Long userId = createTestUserId();

        repository.saveAndFlush(
                new TrainingGoalEntity(userId, 1, createGoal())
        );

        repository.saveAndFlush(
                new TrainingGoalEntity(userId, 2, createGoal())
        );

        entityManager.clear();

        TrainingGoalEntity latest = repository
                .findFirstByUserIdOrderByVersionDesc(userId)
                .orElseThrow();

        assertEquals(2, latest.getVersion());
        assertNotNull(latest.getCreatedAt());
    }

}

