package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingGoalTest {


    private List<TrainingGoalType> createAllGoals() {
        return List.of(
                TrainingGoalType.MUSCLE_GAIN,
                TrainingGoalType.STRENGTH,
                TrainingGoalType.FAT_LOSS,
                TrainingGoalType.ENDURANCE,
                TrainingGoalType.SPORT_PERFORMANCE
        );
    }


    // ==================== Valid Training Goal ====================

    @Test
    void shouldCreateValidTrainingGoal() {

        TrainingGoal goal =
                new TrainingGoal(
                        createAllGoals(),
                        "Improve chest development"
                );

        assertEquals(
                5,
                goal.getGoalPriorities().size()
        );

        assertEquals(
                TrainingGoalType.MUSCLE_GAIN,
                goal.getGoalPriorities().get(0)
        );

        assertEquals(
                "Improve chest development",
                goal.getSpecificGoalDetails()
        );
    }


    // ==================== Goal Priorities ====================

    @Test
    void shouldPreserveGoalPriorityOrder() {

        List<TrainingGoalType> priorities =
                List.of(
                        TrainingGoalType.STRENGTH,
                        TrainingGoalType.MUSCLE_GAIN,
                        TrainingGoalType.SPORT_PERFORMANCE,
                        TrainingGoalType.ENDURANCE,
                        TrainingGoalType.FAT_LOSS
                );

        TrainingGoal goal =
                new TrainingGoal(
                        priorities,
                        null
                );

        assertEquals(
                TrainingGoalType.STRENGTH,
                goal.getGoalPriorities().get(0)
        );

        assertEquals(
                TrainingGoalType.MUSCLE_GAIN,
                goal.getGoalPriorities().get(1)
        );

        assertEquals(
                TrainingGoalType.FAT_LOSS,
                goal.getGoalPriorities().get(4)
        );
    }


    @Test
    void shouldRejectNullGoalPriorities() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingGoal(
                        null,
                        null
                )
        );
    }


    @Test
    void shouldRejectMissingGoal() {

        List<TrainingGoalType> priorities =
                List.of(
                        TrainingGoalType.MUSCLE_GAIN,
                        TrainingGoalType.STRENGTH,
                        TrainingGoalType.FAT_LOSS,
                        TrainingGoalType.ENDURANCE
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
    void shouldRejectDuplicateGoals() {

        List<TrainingGoalType> priorities =
                List.of(
                        TrainingGoalType.MUSCLE_GAIN,
                        TrainingGoalType.MUSCLE_GAIN,
                        TrainingGoalType.STRENGTH,
                        TrainingGoalType.ENDURANCE,
                        TrainingGoalType.SPORT_PERFORMANCE
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
    void shouldRejectNullGoalInsidePriorityList() {

        List<TrainingGoalType> priorities =
                new ArrayList<>();

        priorities.add(TrainingGoalType.MUSCLE_GAIN);
        priorities.add(TrainingGoalType.STRENGTH);
        priorities.add(null);
        priorities.add(TrainingGoalType.ENDURANCE);
        priorities.add(TrainingGoalType.SPORT_PERFORMANCE);

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingGoal(
                        priorities,
                        null
                )
        );
    }


    // ==================== Defensive Copy ====================

    @Test
    void shouldProtectGoalPrioritiesFromExternalModification() {

        List<TrainingGoalType> priorities =
                new ArrayList<>(createAllGoals());

        TrainingGoal goal =
                new TrainingGoal(
                        priorities,
                        null
                );

        priorities.clear();

        assertEquals(
                5,
                goal.getGoalPriorities().size()
        );
    }


    @Test
    void returnedGoalPrioritiesShouldNotBeModifiable() {

        TrainingGoal goal =
                new TrainingGoal(
                        createAllGoals(),
                        null
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> goal.getGoalPriorities().clear()
        );
    }


    // ==================== Specific Goal Details ====================

    @Test
    void shouldAllowNullSpecificGoalDetails() {

        TrainingGoal goal =
                new TrainingGoal(
                        createAllGoals(),
                        null
                );

        assertNull(
                goal.getSpecificGoalDetails()
        );
    }


    @Test
    void blankSpecificGoalDetailsShouldBecomeNull() {

        TrainingGoal goal =
                new TrainingGoal(
                        createAllGoals(),
                        "     "
                );

        assertNull(
                goal.getSpecificGoalDetails()
        );
    }


    @Test
    void shouldStoreSpecificGoalDetails() {

        TrainingGoal goal =
                new TrainingGoal(
                        createAllGoals(),
                        "Increase upper-body muscle"
                );

        assertEquals(
                "Increase upper-body muscle",
                goal.getSpecificGoalDetails()
        );
    }


    @Test
    void shouldStripSpecificGoalDetails() {

        TrainingGoal goal =
                new TrainingGoal(
                        createAllGoals(),
                        "   Increase upper-body muscle   "
                );

        assertEquals(
                "Increase upper-body muscle",
                goal.getSpecificGoalDetails()
        );
    }
}