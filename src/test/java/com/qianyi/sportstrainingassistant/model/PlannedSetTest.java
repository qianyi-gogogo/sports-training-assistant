package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlannedSetTest {

    @Test
    void shouldCreateValidWarmUpSet() {

        PlannedSet set = new PlannedSet(
                SetType.WARM_UP,
                40.0,
                8,
                10
        );

        assertEquals(SetType.WARM_UP, set.getSetType());
        assertEquals(40.0, set.getWeightKg());
        assertEquals(8, set.getMinReps());
        assertEquals(10, set.getMaxReps());
    }


    @Test
    void shouldCreateValidWorkingSet() {

        PlannedSet set = new PlannedSet(
                SetType.WORKING,
                70.0,
                4,
                5
        );

        assertEquals(SetType.WORKING, set.getSetType());
        assertEquals(70.0, set.getWeightKg());
        assertEquals(4, set.getMinReps());
        assertEquals(5, set.getMaxReps());
    }


    // ==================== Validation ====================

    @Test
    void shouldRejectNullSetType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        null,
                        70.0,
                        4,
                        6
                )
        );
    }


    @Test
    void shouldRejectNullWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        null,
                        4,
                        6
                )
        );
    }


    @Test
    void shouldRejectWeightBelowMinimum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        -500.1,
                        4,
                        6
                )
        );
    }


    @Test
    void shouldRejectWeightAboveMaximum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        500.1,
                        4,
                        6
                )
        );
    }


    @Test
    void shouldAllowZeroWeightForBodyweightExercise() {

        PlannedSet set = new PlannedSet(
                SetType.WORKING,
                0.0,
                8,
                12
        );

        assertEquals(0.0, set.getWeightKg());
    }


    @Test
    void shouldAllowNegativeWeightForAssistedExercise() {

        PlannedSet set = new PlannedSet(
                SetType.WORKING,
                -20.0,
                8,
                12
        );

        assertEquals(-20.0, set.getWeightKg());
    }


    @Test
    void shouldRejectNullMinReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        null,
                        6
                )
        );
    }


    @Test
    void shouldRejectNullMaxReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        4,
                        null
                )
        );
    }


    @Test
    void shouldRejectMinRepsBelowOne() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        0,
                        6
                )
        );
    }


    @Test
    void shouldRejectMaxRepsLowerThanMinReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        8,
                        6
                )
        );
    }
}
