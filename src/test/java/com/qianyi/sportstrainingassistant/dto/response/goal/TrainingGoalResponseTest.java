
package com.qianyi.sportstrainingassistant.dto.response.goal;

import com.qianyi.sportstrainingassistant.entity.TrainingGoalEntity;
import com.qianyi.sportstrainingassistant.model.TrainingGoal;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.qianyi.sportstrainingassistant.model.TrainingGoalType.*;
import static org.junit.jupiter.api.Assertions.*;

class TrainingGoalResponseTest {

    private List<com.qianyi.sportstrainingassistant.model.TrainingGoalType>
    createPriorities() {
        return List.of(
                MUSCLE_GAIN,
                STRENGTH,
                FAT_LOSS,
                ENDURANCE,
                SPORT_PERFORMANCE
        );
    }

    @Test
    void shouldCreateResponse() {
        LocalDateTime createdAt =
                LocalDateTime.of(2026, 11, 2, 12, 0);

        TrainingGoalResponse response =
                new TrainingGoalResponse(
                        2,
                        createdAt,
                        createPriorities(),
                        "Improve chest development"
                );

        assertEquals(2, response.version());
        assertEquals(createdAt, response.createdAt());
        assertEquals(MUSCLE_GAIN,
                response.goalPriorities().get(0));
        assertEquals("Improve chest development",
                response.specificGoalDetails());
    }

    @Test
    void shouldConvertEntityToResponse() {
        TrainingGoal goal = new TrainingGoal(
                createPriorities(),
                "Improve strength"
        );

        TrainingGoalEntity entity =
                new TrainingGoalEntity(1L, 3, goal);

        TrainingGoalResponse response =
                TrainingGoalResponse.fromEntity(entity);

        assertEquals(3, response.version());
        assertEquals(createPriorities(),
                response.goalPriorities());
        assertEquals("Improve strength",
                response.specificGoalDetails());

        // An unsaved entity has no creation timestamp yet.
        assertNull(response.createdAt());
    }

    @Test
    void shouldProtectPrioritiesFromModification() {
        var priorities = new ArrayList<>(createPriorities());

        TrainingGoalResponse response =
                new TrainingGoalResponse(
                        1,
                        LocalDateTime.now(),
                        priorities,
                        null
                );

        priorities.clear();

        assertEquals(5, response.goalPriorities().size());

        assertThrows(
                UnsupportedOperationException.class,
                () -> response.goalPriorities().clear()
        );
    }
}
