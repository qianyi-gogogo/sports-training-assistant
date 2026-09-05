package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PullUpRecordTest {
    // ==================== External Weight ====================

    @Test
    void externalWeightShouldAcceptZero() {

        PullUpRecord record = new PullUpRecord();

        assertDoesNotThrow(
                () -> record.setExternalWeight(0)
        );
    }

    @Test
    void externalWeightShouldRejectNegativeValue() {

        PullUpRecord record = new PullUpRecord();

        assertThrows(
                IllegalArgumentException.class,
                () -> record.setExternalWeight(-5)
        );
    }


    // ==================== Reps ====================

    @Test
    void repsShouldAcceptPositiveValue() {

        PullUpRecord record = new PullUpRecord();

        assertDoesNotThrow(
                () -> record.setReps(5)
        );
    }

    @Test
    void repsShouldRejectZero() {

        PullUpRecord record = new PullUpRecord();

        assertThrows(
                IllegalArgumentException.class,
                () -> record.setReps(0)
        );
    }


    // ==================== RIR ====================

    @Test
    void rirShouldAcceptZero() {

        PullUpRecord record = new PullUpRecord();

        assertDoesNotThrow(
                () -> record.setRir(0)
        );
    }

    @Test
    void rirShouldAcceptFive() {

        PullUpRecord record = new PullUpRecord();

        assertDoesNotThrow(
                () -> record.setRir(5)
        );
    }

    @Test
    void rirShouldRejectNegativeValue() {

        PullUpRecord record = new PullUpRecord();

        assertThrows(
                IllegalArgumentException.class,
                () -> record.setRir(-1)
        );
    }

    @Test
    void rirShouldRejectValueAboveFive() {

        PullUpRecord record = new PullUpRecord();

        assertThrows(
                IllegalArgumentException.class,
                () -> record.setRir(6)
        );
    }
}
