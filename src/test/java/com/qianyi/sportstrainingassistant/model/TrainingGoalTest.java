package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingGoalTest {

    // ==================== Test Helpers ====================

    private List<TrainingGoalType> validGoalPriorities() {
        return List.of(
                TrainingGoalType.MUSCLE_GAIN,
                TrainingGoalType.FAT_LOSS,
                TrainingGoalType.STRENGTH,
                TrainingGoalType.ENDURANCE,
                TrainingGoalType.SPORT_PERFORMANCE
        );
    }

    private TrainingGoal createValidTrainingGoal() {
        return new TrainingGoal(
                validGoalPriorities(),
                null
        );
    }


    // ==================== Goal Priorities ====================

    @Test
    void shouldSetAllFiveGoalPriorities() {

        TrainingGoal goal = createValidTrainingGoal();

        assertEquals(
                validGoalPriorities(),
                goal.getGoalPriorities()
        );
    }


    @Test
    void shouldPreserveGoalPriorityOrder() {

        TrainingGoal goal = createValidTrainingGoal();

        List<TrainingGoalType> priorities = List.of(
                TrainingGoalType.STRENGTH,
                TrainingGoalType.MUSCLE_GAIN,
                TrainingGoalType.SPORT_PERFORMANCE,
                TrainingGoalType.ENDURANCE,
                TrainingGoalType.FAT_LOSS
        );

        goal.setGoalPriorities(priorities);

        assertEquals(priorities, goal.getGoalPriorities());
    }


    @Test
    void shouldRejectNullGoalPriorities() {

        TrainingGoal goal = createValidTrainingGoal();

        assertThrows(
                IllegalArgumentException.class,
                () -> goal.setGoalPriorities(null)
        );
    }


    @Test
    void shouldRejectIncompleteGoalPriorities() {

        TrainingGoal goal = createValidTrainingGoal();

        List<TrainingGoalType> priorities = List.of(
                TrainingGoalType.MUSCLE_GAIN,
                TrainingGoalType.STRENGTH
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> goal.setGoalPriorities(priorities)
        );
    }


    @Test
    void shouldRejectNullInsideGoalPriorities() {

        TrainingGoal goal = createValidTrainingGoal();

        List<TrainingGoalType> priorities = new ArrayList<>();
        priorities.add(TrainingGoalType.MUSCLE_GAIN);
        priorities.add(TrainingGoalType.FAT_LOSS);
        priorities.add(TrainingGoalType.STRENGTH);
        priorities.add(TrainingGoalType.ENDURANCE);
        priorities.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> goal.setGoalPriorities(priorities)
        );
    }


    @Test
    void shouldRejectDuplicateGoalPriorities() {

        TrainingGoal goal = createValidTrainingGoal();

        List<TrainingGoalType> priorities = List.of(
                TrainingGoalType.MUSCLE_GAIN,
                TrainingGoalType.FAT_LOSS,
                TrainingGoalType.STRENGTH,
                TrainingGoalType.ENDURANCE,
                TrainingGoalType.MUSCLE_GAIN
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> goal.setGoalPriorities(priorities)
        );
    }


    @Test
    void shouldProtectGoalPrioritiesFromExternalModification() {

        List<TrainingGoalType> priorities = new ArrayList<>(
                validGoalPriorities()
        );

        TrainingGoal goal = new TrainingGoal(
                priorities,
                null
        );

        priorities.set(
                0,
                TrainingGoalType.STRENGTH
        );

        assertEquals(
                TrainingGoalType.MUSCLE_GAIN,
                goal.getGoalPriorities().get(0)
        );
    }


    // ==================== Specific Goal Details ====================

    @Test
    void shouldAcceptSpecificGoalDetails() {

        TrainingGoal goal = new TrainingGoal(
                validGoalPriorities(),
                "I want to prioritize chest development."
        );

        assertEquals(
                "I want to prioritize chest development.",
                goal.getSpecificGoalDetails()
        );
    }


    @Test
    void shouldAllowNullSpecificGoalDetails() {

        TrainingGoal goal = new TrainingGoal(
                validGoalPriorities(),
                null
        );

        assertNull(goal.getSpecificGoalDetails());
    }


    // ==================== Constructor Validation ====================

    @Test
    void shouldRejectNullGoalPrioritiesInConstructor() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingGoal(
                        null,
                        null
                )
        );
    }


    @Test
    void shouldRejectIncompleteGoalPrioritiesInConstructor() {

        List<TrainingGoalType> priorities = List.of(
                TrainingGoalType.MUSCLE_GAIN,
                TrainingGoalType.STRENGTH
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingGoal(
                        priorities,
                        null
                )
        );
    }


    @Test
    void shouldRejectDuplicateGoalPrioritiesInConstructor() {

        List<TrainingGoalType> priorities = List.of(
                TrainingGoalType.MUSCLE_GAIN,
                TrainingGoalType.FAT_LOSS,
                TrainingGoalType.STRENGTH,
                TrainingGoalType.ENDURANCE,
                TrainingGoalType.MUSCLE_GAIN
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingGoal(
                        priorities,
                        null
                )
        );
    }
}
