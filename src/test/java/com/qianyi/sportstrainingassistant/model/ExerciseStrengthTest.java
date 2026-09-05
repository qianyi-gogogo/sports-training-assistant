package com.qianyi.sportstrainingassistant.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ExerciseStrengthTest {
    // ==================== 1-2 Reps ====================

    @Test
    void oneToTwoShouldAcceptValidReps() {

        StrengthRecord record = new StrengthRecord();
        record.setWeight(100.0);
        record.setReps(2);
        record.setRir(1);

        ExerciseStrength strength = new ExerciseStrength();

        assertDoesNotThrow(
                () -> strength.setOneToTwo(record)
        );
}
    @Test
    void oneToTwoShouldRejectInvalidReps() {

        StrengthRecord record = new StrengthRecord();
        record.setWeight(100.0);
        record.setReps(3);
        record.setRir(1);

        ExerciseStrength strength = new ExerciseStrength();

        assertThrows(
                IllegalArgumentException.class,
                () -> strength.setOneToTwo(record)
        );
    }
    // ==================== 4-6 Reps ====================

    @Test
    void fourToSixShouldAcceptValidReps() {

        StrengthRecord record = record(80.0, 5, 1);
        ExerciseStrength strength = new ExerciseStrength();

        assertDoesNotThrow(
                () -> strength.setFourToSix(record)
        );
    }

    @Test
    void fourToSixShouldRejectInvalidReps() {

        StrengthRecord record = record(80.0, 7, 1);
        ExerciseStrength strength = new ExerciseStrength();

        assertThrows(
                IllegalArgumentException.class,
                () -> strength.setFourToSix(record)
        );
    }


    // ==================== 10-12 Reps ====================

    @Test
    void tenToTwelveShouldAcceptValidReps() {

        StrengthRecord record = record(60.0, 11, 1);
        ExerciseStrength strength = new ExerciseStrength();

        assertDoesNotThrow(
                () -> strength.setTenToTwelve(record)
        );
    }

    @Test
    void tenToTwelveShouldRejectInvalidReps() {

        StrengthRecord record = record(60.0, 9, 1);
        ExerciseStrength strength = new ExerciseStrength();

        assertThrows(
                IllegalArgumentException.class,
                () -> strength.setTenToTwelve(record)
        );
    }

    // ==================== Correct Weight Order ====================

    @Test
    void shouldAcceptCorrectWeightOrder() {

        StrengthRecord oneToTwo = record(100.0, 2, 1);
        StrengthRecord fourToSix = record(80.0, 5, 1);
        StrengthRecord tenToTwelve = record(60.0, 10, 1);

        ExerciseStrength strength = new ExerciseStrength();

        assertDoesNotThrow(() -> {
            strength.setOneToTwo(oneToTwo);
            strength.setFourToSix(fourToSix);
            strength.setTenToTwelve(tenToTwelve);
        });
    }


    // ==================== Wrong 1-2 / 4-6 Order ====================

    @Test
    void shouldRejectOneToTwoLighterThanFourToSix() {

        StrengthRecord oneToTwo = record(70.0, 2, 1);
        StrengthRecord fourToSix = record(80.0, 5, 1);

        ExerciseStrength strength = new ExerciseStrength();

        strength.setOneToTwo(oneToTwo);

        assertThrows(
                IllegalArgumentException.class,
                () -> strength.setFourToSix(fourToSix)
        );
    }


    // ==================== Wrong 4-6 / 10-12 Order ====================

    @Test
    void shouldRejectFourToSixLighterThanTenToTwelve() {

        StrengthRecord fourToSix = record(60.0, 5, 1);
        StrengthRecord tenToTwelve = record(70.0, 10, 1);

        ExerciseStrength strength = new ExerciseStrength();

        strength.setFourToSix(fourToSix);

        assertThrows(
                IllegalArgumentException.class,
                () -> strength.setTenToTwelve(tenToTwelve)
        );
    }


    // ==================== Equal Weight ====================

    @Test
    void shouldAcceptEqualWeights() {

        StrengthRecord oneToTwo = record(80.0, 2, 1);
        StrengthRecord fourToSix = record(80.0, 5, 1);
        StrengthRecord tenToTwelve = record(80.0, 10, 1);

        ExerciseStrength strength = new ExerciseStrength();

        assertDoesNotThrow(() -> {
            strength.setOneToTwo(oneToTwo);
            strength.setFourToSix(fourToSix);
            strength.setTenToTwelve(tenToTwelve);
        });
    }
// ==================== Retest Recommendation ====================

    /*
     * Equal weights across adjacent rep ranges are allowed,
     * but they may indicate that the strength data is not precise enough.
     *
     * Equal adjacent weights:
     * -> valid data
     * -> recommend retest
     *
     * Clearly different weights:
     * -> valid data
     * -> no retest recommendation
     */
    @Test
    void equalWeightsShouldRecommendRetest() {
        // Equal weights in adjacent rep ranges should trigger a retest recommendation

        StrengthRecord oneToTwo = record(80.0, 2, 1);
        StrengthRecord fourToSix = record(80.0, 5, 1);
        StrengthRecord tenToTwelve = record(70.0, 10, 1);

        ExerciseStrength strength = new ExerciseStrength();

        strength.setOneToTwo(oneToTwo);
        strength.setFourToSix(fourToSix);
        strength.setTenToTwelve(tenToTwelve);

        assertTrue(strength.needsRetest());
    }

    @Test
    void differentWeightsShouldNotRecommendRetest() {
        // Clearly different weights should not trigger a retest recommendation

        StrengthRecord oneToTwo = record(100.0, 2, 1);
        StrengthRecord fourToSix = record(80.0, 5, 1);
        StrengthRecord tenToTwelve = record(60.0, 10, 1);

        ExerciseStrength strength = new ExerciseStrength();

        strength.setOneToTwo(oneToTwo);
        strength.setFourToSix(fourToSix);
        strength.setTenToTwelve(tenToTwelve);

        assertFalse(strength.needsRetest());
    }

    // ==================== Helper ====================
    private StrengthRecord record(
            double weight,
            int reps,
            int rir) {

        StrengthRecord record = new StrengthRecord();

        record.setWeight(weight);
        record.setReps(reps);
        record.setRir(rir);

        return record;
    }
}
