
package com.qianyi.sportstrainingassistant.entity;

import com.qianyi.sportstrainingassistant.model.TrainingGoal;
import com.qianyi.sportstrainingassistant.model.TrainingGoalType;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static com.qianyi.sportstrainingassistant.model.TrainingGoalType.*;

public class TrainingGoalEntityTest {

    private TrainingGoal createGoal() {
        return new TrainingGoal(
                List.of(
                        MUSCLE_GAIN,
                        STRENGTH,
                        FAT_LOSS,
                        SPORT_PERFORMANCE,
                        ENDURANCE
                ),
                "Improve chest development"
        );
    }

    @Test
    void shouldCreateEntityFromModel() {
        TrainingGoalEntity entity =
                new TrainingGoalEntity(1L, createGoal());

        assertEquals(1L, entity.getUserId());
        assertNull(entity.getId());

        assertEquals(
                MUSCLE_GAIN,
                entity.getGoalPriorities().get(0)
        );

        assertEquals(
                "Improve chest development",
                entity.getSpecificGoalDetails()
        );
    }

    @Test
    void shouldConvertEntityBackToModel() {
        TrainingGoalEntity entity =
                new TrainingGoalEntity(1L, createGoal());

        TrainingGoal result = entity.toModel();

        assertEquals(
                createGoal().getGoalPriorities(),
                result.getGoalPriorities()
        );

        assertEquals(
                "Improve chest development",
                result.getSpecificGoalDetails()
        );
    }


    @Test
    void shouldRejectInvalidUserId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingGoalEntity(0L, createGoal())
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingGoalEntity(null, createGoal())
        );
    }

    @Test
    void shouldRejectNullGoal() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingGoalEntity(1L, null)
        );
    }

    @Test
    void shouldPreventExternalListModification() {
        TrainingGoalEntity entity =
                new TrainingGoalEntity(1L, createGoal());

        assertThrows(
                UnsupportedOperationException.class,
                () -> entity.getGoalPriorities().clear()
        );
    }

    // Verify that different versions preserve
    // their original training goal data.
    @Test
    void shouldPreserveOriginalGoalWhenCreatingNewVersion() {

        TrainingGoal originalGoal = createGoal();

        TrainingGoalEntity version1 =
                new TrainingGoalEntity(
                        1L, 1, originalGoal
                );

        TrainingGoal updatedGoal = new TrainingGoal(
                List.of(
                        STRENGTH,
                        MUSCLE_GAIN,
                        ENDURANCE,
                        FAT_LOSS,
                        SPORT_PERFORMANCE
                ),
                "Improve maximum strength"
        );

        TrainingGoalEntity version2 =
                new TrainingGoalEntity(
                        1L, 2, updatedGoal
                );

        // Check the original version.
        assertEquals(1, version1.getVersion());

        assertEquals(
                MUSCLE_GAIN,
                version1.getGoalPriorities().get(0)
        );

        assertEquals(
                "Improve chest development",
                version1.getSpecificGoalDetails()
        );

        // Check the new version.
        assertEquals(2, version2.getVersion());

        assertEquals(
                STRENGTH,
                version2.getGoalPriorities().get(0)
        );

        assertEquals(
                "Improve maximum strength",
                version2.getSpecificGoalDetails()
        );

        assertEquals(
                version1.getUserId(),
                version2.getUserId()
        );
    }

}
