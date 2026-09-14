package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompletedSetTest {

    // =========================
    // Normal cases
    // =========================

    @Test
    void shouldSetAndGetValidWorkingSet() {
        CompletedSet set = new CompletedSet();

        set.setSetType(SetType.WORKING);
        set.setWeightKg(70.0);
        set.setReps(6);
        set.setRir(1);

        assertEquals(SetType.WORKING, set.getSetType());
        assertEquals(70.0, set.getWeightKg());
        assertEquals(6, set.getReps());
        assertEquals(1, set.getRir());
    }

    @Test
    void shouldSetAndGetValidWarmUpSet() {
        CompletedSet set = new CompletedSet();

        set.setSetType(SetType.WARM_UP);
        set.setWeightKg(40.0);
        set.setReps(10);
        set.setRir(4);

        assertEquals(SetType.WARM_UP, set.getSetType());
        assertEquals(40.0, set.getWeightKg());
        assertEquals(10, set.getReps());
        assertEquals(4, set.getRir());
    }

    // =========================
    // SetType validation
    // =========================

    @Test
    void shouldRejectNullSetType() {
        CompletedSet set = new CompletedSet();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setSetType(null)
        );
    }

    // =========================
    // Weight validation
    // =========================

    @Test
    void shouldAcceptMinimumWeight() {
        CompletedSet set = new CompletedSet();

        set.setWeightKg(-500.0);

        assertEquals(-500.0, set.getWeightKg());
    }

    @Test
    void shouldAcceptMaximumWeight() {
        CompletedSet set = new CompletedSet();

        set.setWeightKg(500.0);

        assertEquals(500.0, set.getWeightKg());
    }

    @Test
    void shouldAcceptZeroWeightForBodyweightExercise() {
        CompletedSet set = new CompletedSet();

        set.setWeightKg(0.0);

        assertEquals(0.0, set.getWeightKg());
    }

    @Test
    void shouldAcceptNegativeWeightForAssistedExercise() {
        CompletedSet set = new CompletedSet();

        set.setWeightKg(-20.0);

        assertEquals(-20.0, set.getWeightKg());
    }

    @Test
    void shouldRejectWeightBelowMinimum() {
        CompletedSet set = new CompletedSet();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setWeightKg(-500.1)
        );
    }

    @Test
    void shouldRejectWeightAboveMaximum() {
        CompletedSet set = new CompletedSet();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setWeightKg(500.1)
        );
    }

    @Test
    void shouldRejectNullWeight() {
        CompletedSet set = new CompletedSet();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setWeightKg(null)
        );
    }

    // =========================
    // Reps validation
    // =========================

    @Test
    void shouldAcceptOneRep() {
        CompletedSet set = new CompletedSet();

        set.setReps(1);

        assertEquals(1, set.getReps());
    }

    @Test
    void shouldAcceptHighRepCount() {
        CompletedSet set = new CompletedSet();

        set.setReps(100);

        assertEquals(100, set.getReps());
    }

    @Test
    void shouldRejectZeroReps() {
        CompletedSet set = new CompletedSet();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setReps(0)
        );
    }

    @Test
    void shouldRejectNegativeReps() {
        CompletedSet set = new CompletedSet();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setReps(-1)
        );
    }

    @Test
    void shouldRejectNullReps() {
        CompletedSet set = new CompletedSet();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setReps(null)
        );
    }

    // =========================
    // RIR validation
    // =========================

    @Test
    void shouldAcceptMinimumRir() {
        CompletedSet set = new CompletedSet();

        set.setRir(0);

        assertEquals(0, set.getRir());
    }

    @Test
    void shouldAcceptMaximumRir() {
        CompletedSet set = new CompletedSet();

        set.setRir(5);

        assertEquals(5, set.getRir());
    }

    @Test
    void shouldAcceptNullRir() {
        CompletedSet set = new CompletedSet();

        set.setRir(null);

        assertNull(set.getRir());
    }

    @Test
    void shouldRejectRirBelowMinimum() {
        CompletedSet set = new CompletedSet();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setRir(-1)
        );
    }

    @Test
    void shouldRejectRirAboveMaximum() {
        CompletedSet set = new CompletedSet();

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setRir(6)
        );
    }

    // =========================
    // Validate first, mutate second
    // =========================

    @Test
    void shouldKeepPreviousWeightWhenInvalidWeightIsProvided() {
        CompletedSet set = new CompletedSet();

        set.setWeightKg(70.0);

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setWeightKg(600.0)
        );

        assertEquals(70.0, set.getWeightKg());
    }

    @Test
    void shouldKeepPreviousRepsWhenInvalidRepsAreProvided() {
        CompletedSet set = new CompletedSet();

        set.setReps(8);

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setReps(0)
        );

        assertEquals(8, set.getReps());
    }

    @Test
    void shouldKeepPreviousRirWhenInvalidRirIsProvided() {
        CompletedSet set = new CompletedSet();

        set.setRir(2);

        assertThrows(
                IllegalArgumentException.class,
                () -> set.setRir(6)
        );

        assertEquals(2, set.getRir());
    }
}
