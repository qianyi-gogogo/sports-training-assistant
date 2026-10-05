package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PullUpRecordTest {


    // ==================== Valid Records ====================

    @Test
    void shouldCreateBodyweightPullUpRecord() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        10,
                        1
                );

        assertEquals(PullUpMode.BODYWEIGHT, record.getMode());
        assertEquals(0.0, record.getWeightKg(), 0.001);
        assertEquals(10, record.getReps());
        assertEquals(1, record.getRir());
    }


    @Test
    void shouldCreateWeightedPullUpRecord() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        20.0,
                        5,
                        1
                );

        assertEquals(PullUpMode.WEIGHTED, record.getMode());
        assertEquals(20.0, record.getWeightKg(), 0.001);
        assertEquals(5, record.getReps());
        assertEquals(1, record.getRir());
    }


    @Test
    void shouldCreateAssistedPullUpRecord() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.ASSISTED,
                        25.0,
                        8,
                        2
                );

        assertEquals(PullUpMode.ASSISTED, record.getMode());
        assertEquals(25.0, record.getWeightKg(), 0.001);
        assertEquals(8, record.getReps());
        assertEquals(2, record.getRir());
    }


    // ==================== Mode ====================

    @Test
    void shouldRejectNullMode() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        null,
                        0.0,
                        10,
                        1
                )
        );
    }


    // ==================== Bodyweight ====================

    @Test
    void bodyweightShouldRejectPositiveWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        10.0,
                        10,
                        1
                )
        );
    }


    // ==================== Weighted ====================

    @Test
    void weightedShouldRejectZeroWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        0.0,
                        5,
                        1
                )
        );
    }


    @Test
    void weightedShouldAcceptPositiveWeight() {

        assertDoesNotThrow(
                () -> new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        20.0,
                        5,
                        1
                )
        );
    }


    // ==================== Assisted ====================

    @Test
    void assistedShouldRejectZeroWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.ASSISTED,
                        0.0,
                        8,
                        1
                )
        );
    }


    @Test
    void assistedShouldAcceptPositiveWeight() {

        assertDoesNotThrow(
                () -> new PullUpRecord(
                        PullUpMode.ASSISTED,
                        25.0,
                        8,
                        1
                )
        );
    }


    // ==================== Weight ====================

    @Test
    void shouldRejectNegativeWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.ASSISTED,
                        -10.0,
                        8,
                        1
                )
        );
    }


    @Test
    void shouldAcceptMaximumWeight() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        500.0,
                        1,
                        0
                );

        assertEquals(500.0, record.getWeightKg(), 0.001);
    }


    @Test
    void shouldRejectWeightAboveMaximum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        500.1,
                        1,
                        0
                )
        );
    }


    @Test
    void shouldRejectNaNWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        Double.NaN,
                        5,
                        1
                )
        );
    }


    @Test
    void shouldRejectInfiniteWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        Double.POSITIVE_INFINITY,
                        5,
                        1
                )
        );
    }


    // ==================== Reps ====================

    @Test
    void shouldAcceptPositiveReps() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        1,
                        1
                );

        assertEquals(1, record.getReps());
    }


    @Test
    void shouldRejectZeroReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        0,
                        1
                )
        );
    }


    @Test
    void shouldRejectNegativeReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        -1,
                        1
                )
        );
    }


    // ==================== RIR ====================

    @Test
    void shouldAcceptNullRir() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        10,
                        null
                );

        assertNull(record.getRir());
    }


    @Test
    void shouldAcceptRirBoundaryValues() {

        PullUpRecord zeroRir =
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        10,
                        0
                );

        PullUpRecord fiveRir =
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        10,
                        5
                );

        assertEquals(0, zeroRir.getRir());
        assertEquals(5, fiveRir.getRir());
    }


    @Test
    void shouldRejectNegativeRir() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        10,
                        -1
                )
        );
    }


    @Test
    void shouldRejectRirAboveFive() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        10,
                        6
                )
        );
    }
}