package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlannedSetTest {


    // ==================== Valid Planned Set ====================

    @Test
    void shouldCreateValidPlannedSet() {

        PlannedSet set =
                new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        6,
                        10
                );

        assertEquals(SetType.WORKING, set.getSetType());
        assertEquals(70.0, set.getWeightKg(), 0.001);
        assertEquals(6, set.getMinReps());
        assertEquals(10, set.getMaxReps());
    }


    @Test
    void shouldAcceptWarmUpSet() {

        PlannedSet set =
                new PlannedSet(
                        SetType.WARM_UP,
                        40.0,
                        8,
                        12
                );

        assertEquals(SetType.WARM_UP, set.getSetType());
    }


    // ==================== Set Type ====================

    @Test
    void shouldRejectNullSetType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        null,
                        70.0,
                        6,
                        10
                )
        );
    }


    // ==================== Weight ====================

    @Test
    void shouldAcceptWeightBoundaryValues() {

        PlannedSet minimum =
                new PlannedSet(
                        SetType.WORKING,
                        -500.0,
                        5,
                        8
                );

        PlannedSet maximum =
                new PlannedSet(
                        SetType.WORKING,
                        500.0,
                        5,
                        8
                );

        assertEquals(-500.0, minimum.getWeightKg(), 0.001);
        assertEquals(500.0, maximum.getWeightKg(), 0.001);
    }


    @Test
    void shouldAcceptZeroWeight() {

        PlannedSet set =
                new PlannedSet(
                        SetType.WORKING,
                        0.0,
                        5,
                        8
                );

        assertEquals(0.0, set.getWeightKg(), 0.001);
    }


    @Test
    void shouldAcceptNegativeWeightForAssistance() {

        PlannedSet set =
                new PlannedSet(
                        SetType.WORKING,
                        -20.0,
                        8,
                        12
                );

        assertEquals(-20.0, set.getWeightKg(), 0.001);
    }


    @Test
    void shouldRejectNullWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        null,
                        5,
                        8
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
                        5,
                        8
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
                        5,
                        8
                )
        );
    }


    @Test
    void shouldRejectNaNWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        Double.NaN,
                        5,
                        8
                )
        );
    }


    @Test
    void shouldRejectInfiniteWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        Double.POSITIVE_INFINITY,
                        5,
                        8
                )
        );
    }


    // ==================== Minimum Reps ====================

    @Test
    void shouldAcceptMinimumRepOfOne() {

        PlannedSet set =
                new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        1,
                        1
                );

        assertEquals(1, set.getMinReps());
    }


    @Test
    void shouldRejectNullMinimumReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        null,
                        8
                )
        );
    }


    @Test
    void shouldRejectZeroMinimumReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        0,
                        8
                )
        );
    }


    // ==================== Maximum Reps ====================

    @Test
    void shouldAcceptMaximumRepsEqualToMinimumReps() {

        PlannedSet set =
                new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        5,
                        5
                );

        assertEquals(5, set.getMaxReps());
    }


    @Test
    void shouldAcceptHighRepRange() {

        PlannedSet set =
                new PlannedSet(
                        SetType.WORKING,
                        20.0,
                        100,
                        150
                );

        assertEquals(100, set.getMinReps());
        assertEquals(150, set.getMaxReps());
    }


    @Test
    void shouldRejectNullMaximumReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        5,
                        null
                )
        );
    }


    @Test
    void shouldRejectMaximumRepsBelowMinimumReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        10,
                        8
                )
        );
    }
}