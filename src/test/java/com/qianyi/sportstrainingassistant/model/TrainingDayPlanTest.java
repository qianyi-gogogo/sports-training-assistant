package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingDayPlanTest {

    @Test
    void shouldCreateTrainingDayWithExercises() {

        PlannedSet benchSet = new PlannedSet(
                SetType.WORKING,
                70.0,
                4,
                6
        );

        ExercisePlan benchPress = new ExercisePlan(
                "Bench Press",
                List.of(benchSet)
        );

        TrainingDayPlan dayPlan = new TrainingDayPlan(
                DayOfWeek.MONDAY,
                "Chest + Triceps",
                false,
                List.of(benchPress)
        );

        assertEquals(
                DayOfWeek.MONDAY,
                dayPlan.getDayOfWeek()
        );

        assertEquals(
                "Chest + Triceps",
                dayPlan.getWorkoutName()
        );

        assertFalse(dayPlan.isRestDay());

        assertEquals(
                List.of(benchPress),
                dayPlan.getExercises()
        );
    }


    @Test
    void shouldCreateRestDay() {

        TrainingDayPlan dayPlan = new TrainingDayPlan(
                DayOfWeek.WEDNESDAY,
                null,
                true,
                List.of()
        );

        assertEquals(
                DayOfWeek.WEDNESDAY,
                dayPlan.getDayOfWeek()
        );

        assertNull(dayPlan.getWorkoutName());

        assertTrue(dayPlan.isRestDay());

        assertTrue(
                dayPlan.getExercises().isEmpty()
        );
    }
    // ==================== Validation ====================

    @Test
    void shouldRejectNullDayOfWeek() {

        ExercisePlan exercise = new ExercisePlan(
                "Bench Press",
                List.of(
                        new PlannedSet(
                                SetType.WORKING,
                                70.0,
                                4,
                                6
                        )
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        null,
                        "Chest",
                        false,
                        List.of(exercise)
                )
        );
    }


    @Test
    void shouldRejectNullWorkoutNameForTrainingDay() {

        ExercisePlan exercise = new ExercisePlan(
                "Bench Press",
                List.of(
                        new PlannedSet(
                                SetType.WORKING,
                                70.0,
                                4,
                                6
                        )
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        DayOfWeek.MONDAY,
                        null,
                        false,
                        List.of(exercise)
                )
        );
    }


    @Test
    void shouldRejectBlankWorkoutNameForTrainingDay() {

        ExercisePlan exercise = new ExercisePlan(
                "Bench Press",
                List.of(
                        new PlannedSet(
                                SetType.WORKING,
                                70.0,
                                4,
                                6
                        )
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        DayOfWeek.MONDAY,
                        "   ",
                        false,
                        List.of(exercise)
                )
        );
    }


    @Test
    void shouldRejectNullExercisesForTrainingDay() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        DayOfWeek.MONDAY,
                        "Chest",
                        false,
                        null
                )
        );
    }


    @Test
    void shouldRejectEmptyExercisesForTrainingDay() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        DayOfWeek.MONDAY,
                        "Chest",
                        false,
                        List.of()
                )
        );
    }


    @Test
    void shouldRejectExercisesOnRestDay() {

        ExercisePlan exercise = new ExercisePlan(
                "Bench Press",
                List.of(
                        new PlannedSet(
                                SetType.WORKING,
                                70.0,
                                4,
                                6
                        )
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        DayOfWeek.WEDNESDAY,
                        null,
                        true,
                        List.of(exercise)
                )
        );
    }


    @Test
    void shouldRejectNullInsideExercises() {

        List<ExercisePlan> exercises = new java.util.ArrayList<>();

        exercises.add(
                new ExercisePlan(
                        "Bench Press",
                        List.of(
                                new PlannedSet(
                                        SetType.WORKING,
                                        70.0,
                                        4,
                                        6
                                )
                        )
                )
        );

        exercises.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> new TrainingDayPlan(
                        DayOfWeek.MONDAY,
                        "Chest",
                        false,
                        exercises
                )
        );
    }


    @Test
    void shouldProtectExercisesFromExternalModification() {

        List<ExercisePlan> exercises = new java.util.ArrayList<>();

        ExercisePlan exercise = new ExercisePlan(
                "Bench Press",
                List.of(
                        new PlannedSet(
                                SetType.WORKING,
                                70.0,
                                4,
                                6
                        )
                )
        );

        exercises.add(exercise);

        TrainingDayPlan plan = new TrainingDayPlan(
                DayOfWeek.MONDAY,
                "Chest",
                false,
                exercises
        );

        exercises.clear();

        assertEquals(
                1,
                plan.getExercises().size()
        );

        assertEquals(
                exercise,
                plan.getExercises().get(0)
        );
    }
}
