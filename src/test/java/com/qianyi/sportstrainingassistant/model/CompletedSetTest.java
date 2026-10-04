package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CompletedSetTest {


    // ==================== Valid Completed Set ====================

    @Test
    void shouldCreateValidCompletedSet() {

        CompletedSet set =
                new CompletedSet(
                        SetType.WORKING,
                        70.0,
                        8,
                        1
                );

        assertEquals(SetType.WORKING, set.getSetType());
        assertEquals(70.0, set.getWeightKg(), 0.001);
        assertEquals(8, set.getReps());
        assertEquals(1, set.getRir());
    }


    @Test
    void shouldAllowNullRir() {

        CompletedSet set =
                new CompletedSet(
                        SetType.WORKING,
                        70.0,
                        8,
                        null
                );

        assertNull(set.getRir());
    }


    // ==================== Set Type ====================

    @Test
    void shouldRejectNullSetType() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompletedSet(
                        null,
                        70.0,
                        8,
                        1
                )
        );
    }


    // ==================== Weight ====================

    @Test
    void shouldAcceptWeightBoundaryValues() {

        CompletedSet minimum =
                new CompletedSet(
                        SetType.WORKING,
                        -500.0,
                        8,
                        1
                );

        CompletedSet maximum =
                new CompletedSet(
                        SetType.WORKING,
                        500.0,
                        8,
                        1
                );

        assertEquals(-500.0, minimum.getWeightKg(), 0.001);
        assertEquals(500.0, maximum.getWeightKg(), 0.001);
    }


    @Test
    void shouldAcceptZeroWeight() {

        CompletedSet set =
                new CompletedSet(
                        SetType.WORKING,
                        0.0,
                        10,
                        1
                );

        assertEquals(0.0, set.getWeightKg(), 0.001);
    }


    @Test
    void shouldAcceptNegativeWeightForAssistance() {

        CompletedSet set =
                new CompletedSet(
                        SetType.WORKING,
                        -20.0,
                        10,
                        1
                );

        assertEquals(-20.0, set.getWeightKg(), 0.001);
    }


    @Test
    void shouldRejectWeightBelowMinimum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompletedSet(
                        SetType.WORKING,
                        -500.1,
                        8,
                        1
                )
        );
    }


    @Test
    void shouldRejectWeightAboveMaximum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompletedSet(
                        SetType.WORKING,
                        500.1,
                        8,
                        1
                )
        );
    }


    @Test
    void shouldRejectNaNWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompletedSet(
                        SetType.WORKING,
                        Double.NaN,
                        8,
                        1
                )
        );
    }


    @Test
    void shouldRejectInfiniteWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompletedSet(
                        SetType.WORKING,
                        Double.POSITIVE_INFINITY,
                        8,
                        1
                )
        );
    }


    // ==================== Reps ====================

    @Test
    void shouldAcceptPositiveReps() {

        CompletedSet set =
                new CompletedSet(
                        SetType.WORKING,
                        70.0,
                        1,
                        1
                );

        assertEquals(1, set.getReps());
    }


    @Test
    void shouldAcceptHighRepCount() {

        CompletedSet set =
                new CompletedSet(
                        SetType.WORKING,
                        20.0,
                        150,
                        3
                );

        assertEquals(150, set.getReps());
    }


    @Test
    void shouldRejectZeroReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompletedSet(
                        SetType.WORKING,
                        70.0,
                        0,
                        1
                )
        );
    }


    @Test
    void shouldRejectNegativeReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompletedSet(
                        SetType.WORKING,
                        70.0,
                        -1,
                        1
                )
        );
    }


    // ==================== RIR ====================

    @Test
    void shouldAcceptRirBoundaryValues() {

        CompletedSet zeroRir =
                new CompletedSet(
                        SetType.WORKING,
                        70.0,
                        8,
                        0
                );

        CompletedSet fiveRir =
                new CompletedSet(
                        SetType.WORKING,
                        70.0,
                        8,
                        5
                );

        assertEquals(0, zeroRir.getRir());
        assertEquals(5, fiveRir.getRir());
    }


    @Test
    void shouldRejectNegativeRir() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompletedSet(
                        SetType.WORKING,
                        70.0,
                        8,
                        -1
                )
        );
    }


    @Test
    void shouldRejectRirAboveFive() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CompletedSet(
                        SetType.WORKING,
                        70.0,
                        8,
                        6
                )
        );
    }
}