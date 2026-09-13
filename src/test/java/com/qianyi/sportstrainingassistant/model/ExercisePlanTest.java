package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExercisePlanTest {

    @Test
    void shouldCreateExercisePlanWithNameAndSets() {

        PlannedSet warmUpSet = new PlannedSet(
                SetType.WARM_UP,
                40.0,
                8,
                10
        );

        PlannedSet workingSet = new PlannedSet(
                SetType.WORKING,
                70.0,
                4,
                6
        );

        List<PlannedSet> sets = List.of(
                warmUpSet,
                workingSet
        );

        ExercisePlan exercisePlan = new ExercisePlan(
                "Bench Press",
                sets
        );

        assertEquals(
                "Bench Press",
                exercisePlan.getExerciseName()
        );

        assertEquals(
                sets,
                exercisePlan.getPlannedSets()
        );
    }


    @Test
    void shouldPreserveSetOrder() {

        PlannedSet set1 = new PlannedSet(
                SetType.WARM_UP,
                20.0,
                15,
                15
        );

        PlannedSet set2 = new PlannedSet(
                SetType.WARM_UP,
                40.0,
                8,
                10
        );

        PlannedSet set3 = new PlannedSet(
                SetType.WORKING,
                70.0,
                4,
                5
        );

        List<PlannedSet> sets = List.of(
                set1,
                set2,
                set3
        );

        ExercisePlan exercisePlan = new ExercisePlan(
                "Bench Press",
                sets
        );

        assertEquals(
                set1,
                exercisePlan.getPlannedSets().get(0)
        );

        assertEquals(
                set2,
                exercisePlan.getPlannedSets().get(1)
        );

        assertEquals(
                set3,
                exercisePlan.getPlannedSets().get(2)
        );
    }
    // ==================== Validation ====================

    @Test
    void shouldRejectNullExerciseName() {

        PlannedSet set = new PlannedSet(
                SetType.WORKING,
                70.0,
                4,
                6
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExercisePlan(
                        null,
                        List.of(set)
                )
        );
    }


    @Test
    void shouldRejectBlankExerciseName() {

        PlannedSet set = new PlannedSet(
                SetType.WORKING,
                70.0,
                4,
                6
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExercisePlan(
                        "   ",
                        List.of(set)
                )
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
    void shouldRejectNullInsidePlannedSets() {

        List<PlannedSet> sets = new java.util.ArrayList<>();

        sets.add(
                new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        4,
                        6
                )
        );

        sets.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExercisePlan(
                        "Bench Press",
                        sets
                )
        );
    }


    @Test
    void shouldProtectPlannedSetsFromExternalModification() {

        List<PlannedSet> sets = new java.util.ArrayList<>();

        PlannedSet firstSet = new PlannedSet(
                SetType.WORKING,
                70.0,
                4,
                6
        );

        sets.add(firstSet);

        ExercisePlan exercisePlan = new ExercisePlan(
                "Bench Press",
                sets
        );

        sets.clear();

        assertEquals(
                1,
                exercisePlan.getPlannedSets().size()
        );

        assertEquals(
                firstSet,
                exercisePlan.getPlannedSets().get(0)
        );
    }
}