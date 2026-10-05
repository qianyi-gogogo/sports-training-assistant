package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingDayPlanTest {


    private ExercisePlan createExercisePlan() {

        PlannedSet plannedSet =
                new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        6,
                        10
                );

        return new ExercisePlan(
                "Bench Press",
                List.of(plannedSet)
        );
    }


    // ==================== Training Day ====================

    @Test
    void shouldCreateTrainingDay() {

        ExercisePlan exercise =
                createExercisePlan();

        TrainingDayPlan day =
                new TrainingDayPlan(
                        "Chest Day",
                        DayType.TRAINING,
                        List.of(exercise)
                );

        assertEquals(
                "Chest Day",
                day.getWorkoutName()
        );

        assertEquals(
                DayType.TRAINING,
                day.getDayType()
        );

        assertEquals(
                1,
                day.getExercises().size()
        );

        assertSame(
                exercise,
                day.getExercises().get(0)
        );
    }


    @Test
    void trainingDayShouldRejectEmptyExercises() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        "Chest Day",
                        DayType.TRAINING,
                        List.of()
                )
        );
    }


    // ==================== Rest Day ====================

    @Test
    void shouldCreateRestDay() {

        TrainingDayPlan day =
                new TrainingDayPlan(
                        "Rest Day",
                        DayType.REST,
                        List.of()
                );

        assertEquals(
                DayType.REST,
                day.getDayType()
        );

        assertTrue(
                day.getExercises().isEmpty()
        );
    }


    @Test
    void restDayShouldAllowExercises() {

        ExercisePlan exercise =
                createExercisePlan();

        TrainingDayPlan day =
                new TrainingDayPlan(
                        "Rest Day",
                        DayType.REST,
                        List.of(exercise)
                );

        assertEquals(
                DayType.REST,
                day.getDayType()
        );

        assertEquals(
                1,
                day.getExercises().size()
        );

        assertSame(
                exercise,
                day.getExercises().get(0)
        );
    }


    // ==================== Active Recovery ====================

    @Test
    void activeRecoveryShouldAllowEmptyExercises() {

        TrainingDayPlan day =
                new TrainingDayPlan(
                        "Recovery",
                        DayType.ACTIVE_RECOVERY,
                        List.of()
                );

        assertEquals(
                DayType.ACTIVE_RECOVERY,
                day.getDayType()
        );

        assertTrue(
                day.getExercises().isEmpty()
        );
    }


    @Test
    void activeRecoveryShouldAllowExercises() {

        ExercisePlan exercise =
                createExercisePlan();

        TrainingDayPlan day =
                new TrainingDayPlan(
                        "Light Recovery",
                        DayType.ACTIVE_RECOVERY,
                        List.of(exercise)
                );

        assertEquals(
                DayType.ACTIVE_RECOVERY,
                day.getDayType()
        );

        assertEquals(
                1,
                day.getExercises().size()
        );
    }


    // ==================== Workout Name ====================

    @Test
    void workoutNameShouldBeOptional() {

        TrainingDayPlan day =
                new TrainingDayPlan(
                        null,
                        DayType.REST,
                        List.of()
                );

        assertNull(
                day.getWorkoutName()
        );
    }


    @Test
    void blankWorkoutNameShouldBecomeNull() {

        TrainingDayPlan day =
                new TrainingDayPlan(
                        "     ",
                        DayType.REST,
                        List.of()
                );

        assertNull(
                day.getWorkoutName()
        );
    }


    @Test
    void workoutNameShouldBeTrimmed() {

        TrainingDayPlan day =
                new TrainingDayPlan(
                        "   Chest Day   ",
                        DayType.TRAINING,
                        List.of(createExercisePlan())
                );

        assertEquals(
                "Chest Day",
                day.getWorkoutName()
        );
    }


    // ==================== Day Type ====================

    @Test
    void shouldRejectNullDayType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        "Chest Day",
                        null,
                        List.of(createExercisePlan())
                )
        );
    }


    // ==================== Exercises ====================

    @Test
    void shouldRejectNullExercises() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        "Chest Day",
                        DayType.TRAINING,
                        null
                )
        );
    }


    @Test
    void shouldRejectNullExerciseInsideList() {

        List<ExercisePlan> exercises =
                new ArrayList<>();

        exercises.add(
                createExercisePlan()
        );

        exercises.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        "Chest Day",
                        DayType.TRAINING,
                        exercises
                )
        );
    }


    // ==================== Defensive Copy ====================

    @Test
    void shouldProtectExercisesFromExternalModification() {

        List<ExercisePlan> exercises =
                new ArrayList<>();

        exercises.add(
                createExercisePlan()
        );

        TrainingDayPlan day =
                new TrainingDayPlan(
                        "Chest Day",
                        DayType.TRAINING,
                        exercises
                );

        exercises.clear();

        assertEquals(
                1,
                day.getExercises().size()
        );
    }


    @Test
    void returnedExercisesShouldNotBeModifiable() {

        TrainingDayPlan day =
                new TrainingDayPlan(
                        "Chest Day",
                        DayType.TRAINING,
                        List.of(createExercisePlan())
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> day.getExercises().clear()
        );
    }
}