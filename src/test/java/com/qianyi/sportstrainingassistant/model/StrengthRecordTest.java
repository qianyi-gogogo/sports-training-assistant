package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StrengthRecordTest {


    // ==================== Valid Record ====================

    @Test
    void shouldCreateValidStrengthRecord() {

        StrengthRecord record =
                new StrengthRecord(
                        80.0,
                        5,
                        1
                );

        assertEquals(80.0, record.getWeightKg(), 0.001);
        assertEquals(5, record.getReps());
        assertEquals(1, record.getRir());
    }


    @Test
    void shouldAllowNullRir() {

        StrengthRecord record =
                new StrengthRecord(
                        80.0,
                        5,
                        null
                );

        assertNull(record.getRir());
    }


    // ==================== Weight ====================

    @Test
    void shouldAcceptWeightBoundaryValues() {

        StrengthRecord minimum =
                new StrengthRecord(
                        0.0,
                        5,
                        1
                );

        StrengthRecord maximum =
                new StrengthRecord(
                        500.0,
                        5,
                        1
                );

        assertEquals(0.0, minimum.getWeightKg(), 0.001);
        assertEquals(500.0, maximum.getWeightKg(), 0.001);
    }


    @Test
    void shouldRejectNegativeWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StrengthRecord(
                        -0.1,
                        5,
                        1
                )
        );
    }


    @Test
    void shouldRejectWeightAboveMaximum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StrengthRecord(
                        500.1,
                        5,
                        1
                )
        );
    }


    @Test
    void shouldRejectNaNWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StrengthRecord(
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
                () -> new StrengthRecord(
                        Double.POSITIVE_INFINITY,
                        5,
                        1
                )
        );
    }


    // ==================== Reps ====================

    @Test
    void shouldAcceptOneRep() {

        StrengthRecord record =
                new StrengthRecord(
                        80.0,
                        1,
                        1
                );

        assertEquals(1, record.getReps());
    }


    @Test
    void shouldAcceptHighRepCount() {

        StrengthRecord record =
                new StrengthRecord(
                        20.0,
                        150,
                        3
                );

        assertEquals(150, record.getReps());
    }


    @Test
    void shouldRejectZeroReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StrengthRecord(
                        80.0,
                        0,
                        1
                )
        );
    }


    @Test
    void shouldRejectNegativeReps() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StrengthRecord(
                        80.0,
                        -1,
                        1
                )
        );
    }


    // ==================== RIR ====================

    @Test
    void shouldAcceptRirBoundaryValues() {

        StrengthRecord zeroRir =
                new StrengthRecord(
                        80.0,
                        5,
                        0
                );

        StrengthRecord fiveRir =
                new StrengthRecord(
                        80.0,
                        5,
                        5
                );

        assertEquals(0, zeroRir.getRir());
        assertEquals(5, fiveRir.getRir());
    }


    @Test
    void shouldRejectNegativeRir() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StrengthRecord(
                        80.0,
                        5,
                        -1
                )
        );
    }


    @Test
    void shouldRejectRirAboveFive() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new StrengthRecord(
                        80.0,
                        5,
                        6
                )
        );
    }
}