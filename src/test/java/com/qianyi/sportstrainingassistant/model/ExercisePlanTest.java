package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExercisePlanTest {


    private PlannedSet createSet() {
        return new PlannedSet(
                SetType.WORKING,
                70.0,
                6,
                10
        );
    }


    // ==================== Valid Exercise Plan ====================

    @Test
    void shouldCreateValidExercisePlan() {

        PlannedSet set = createSet();

        ExercisePlan exercise =
                new ExercisePlan(
                        "Bench Press",
                        List.of(set)
                );

        assertEquals(
                "Bench Press",
                exercise.getExerciseName()
        );

        assertEquals(
                1,
                exercise.getPlannedSets().size()
        );

        assertSame(
                set,
                exercise.getPlannedSets().get(0)
        );
    }


    // ==================== Exercise Name ====================

    @Test
    void shouldTrimExerciseName() {

        ExercisePlan exercise =
                new ExercisePlan(
                        "   Bench Press   ",
                        List.of(createSet())
                );

        assertEquals(
                "Bench Press",
                exercise.getExerciseName()
        );
    }


    @Test
    void shouldRejectNullExerciseName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExercisePlan(
                        null,
                        List.of(createSet())
                )
        );
    }


    @Test
    void shouldRejectEmptyExerciseName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExercisePlan(
                        "",
                        List.of(createSet())
                )
        );
    }


    @Test
    void shouldRejectBlankExerciseName() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExercisePlan(
                        "   ",
                        List.of(createSet())
                )
        );
    }


    // ==================== Planned Sets ====================

    @Test
    void shouldAcceptMultiplePlannedSets() {

        PlannedSet set1 =
                new PlannedSet(
                        SetType.WARM_UP,
                        40.0,
                        8,
                        10
                );

        PlannedSet set2 =
                new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        6,
                        8
                );

        PlannedSet set3 =
                new PlannedSet(
                        SetType.WORKING,
                        65.0,
                        8,
                        10
                );

        ExercisePlan exercise =
                new ExercisePlan(
                        "Bench Press",
                        List.of(
                                set1,
                                set2,
                                set3
                        )
                );

        assertEquals(
                3,
                exercise.getPlannedSets().size()
        );
    }


    @Test
    void shouldRejectNullPlannedSets() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExercisePlan(
                        "Bench Press",
                        null
                )
        );
    }


    @Test
    void shouldRejectEmptyPlannedSets() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExercisePlan(
                        "Bench Press",
                        List.of()
                )
        );
    }


    @Test
    void shouldRejectNullPlannedSet() {

        List<PlannedSet> sets =
                new ArrayList<>();

        sets.add(createSet());
        sets.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExercisePlan(
                        "Bench Press",
                        sets
                )
        );
    }


    // ==================== Defensive Copy ====================

    @Test
    void shouldProtectPlannedSetsFromExternalModification() {

        List<PlannedSet> originalSets =
                new ArrayList<>();

        originalSets.add(createSet());

        ExercisePlan exercise =
                new ExercisePlan(
                        "Bench Press",
                        originalSets
                );

        originalSets.clear();

        assertEquals(
                1,
                exercise.getPlannedSets().size()
        );
    }


    @Test
    void returnedPlannedSetsShouldNotBeModifiable() {

        ExercisePlan exercise =
                new ExercisePlan(
                        "Bench Press",
                        List.of(createSet())
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> exercise.getPlannedSets().clear()
        );
    }


    // ==================== No Training Rule Limit ====================

    @Test
    void shouldAcceptManyPlannedSets() {

        List<PlannedSet> sets =
                new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            sets.add(
                    new PlannedSet(
                            SetType.WORKING,
                            50.0,
                            5,
                            10
                    )
            );
        }

        ExercisePlan exercise =
                new ExercisePlan(
                        "Bench Press",
                        sets
                );

        assertEquals(
                20,
                exercise.getPlannedSets().size()
        );
    }
}