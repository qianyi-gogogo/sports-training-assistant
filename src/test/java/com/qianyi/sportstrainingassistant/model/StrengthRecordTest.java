package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StrengthRecordTest {
    // ==================== Weight ====================

    @Test
    void weightShouldAcceptZero() {

        StrengthRecord record = new StrengthRecord();

        assertDoesNotThrow(
                () -> record.setWeight(0.0)
        );
    }

    @Test
    void weightShouldAcceptMaximumValue() {

        StrengthRecord record = new StrengthRecord();

        assertDoesNotThrow(
                () -> record.setWeight(1000.0)
        );
    }

    @Test
    void weightShouldRejectNegativeValue() {

        StrengthRecord record = new StrengthRecord();

        assertThrows(
                IllegalArgumentException.class,
                () -> record.setWeight(-1.0)
        );
    }

    @Test
    void weightShouldRejectValueAboveMaximum() {

        StrengthRecord record = new StrengthRecord();

        assertThrows(
                IllegalArgumentException.class,
                () -> record.setWeight(1001.0)
        );
    }


    // ==================== Reps ====================

    @Test
    void repsShouldAcceptPositiveValue() {

        StrengthRecord record = new StrengthRecord();

        assertDoesNotThrow(
                () -> record.setReps(5)
        );
    }

    @Test
    void repsShouldRejectZero() {

        StrengthRecord record = new StrengthRecord();

        assertThrows(
                IllegalArgumentException.class,
                () -> record.setReps(0)
        );
    }


    // ==================== RIR ====================
    // ==================== RIR Validation ====================

    /*
     * RIR (Reps In Reserve) must be between 0 and 5.
     *
     * Valid range:
     * 0 <= RIR <= 5
     *
     * RIR < 0:
     * -> invalid
     * -> throw IllegalArgumentException
     *
     * RIR > 5:
     * -> invalid
     * -> throw IllegalArgumentException
     */

    @Test
    void rirShouldAcceptZero() {

        StrengthRecord record = new StrengthRecord();

        assertDoesNotThrow(
                () -> record.setRir(0)
        );
    }

    @Test
    void rirShouldAcceptFive() {

        StrengthRecord record = new StrengthRecord();

        assertDoesNotThrow(
                () -> record.setRir(5)
        );
    }

    @Test
    void rirShouldRejectNegativeValue() {

        StrengthRecord record = new StrengthRecord();

        assertThrows(
                IllegalArgumentException.class,
                () -> record.setRir(-1)
        );
    }

    @Test
    void rirShouldRejectValueAboveFive() {

        StrengthRecord record = new StrengthRecord();

        assertThrows(
                IllegalArgumentException.class,
                () -> record.setRir(6)
        );
    }
}
