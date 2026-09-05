package com.qianyi.sportstrainingassistant.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PullUpModeStrengthTest {
    @Test
    void oneToTwoShouldAcceptValidReps() {

        PullUpRecord record = new PullUpRecord();
        record.setExternalWeight(10);
        record.setReps(2);
        record.setRir(1);

        PullUpModeStrength strength = new PullUpModeStrength();

        assertDoesNotThrow(
                () -> strength.setOneToTwo(record)
        );
    }
    @Test
    void oneToTwoShouldRejectInvalidReps() {

        PullUpRecord record = new PullUpRecord();
        record.setExternalWeight(10);
        record.setReps(5);
        record.setRir(1);

        PullUpModeStrength strength = new PullUpModeStrength();

        assertThrows(
                IllegalArgumentException.class,
                () -> strength.setOneToTwo(record)
        );
    }
    @Test
    void fourToSixShouldAcceptValidReps() {

        PullUpRecord record = new PullUpRecord();
        record.setExternalWeight(10);
        record.setReps(5);
        record.setRir(1);

        PullUpModeStrength strength = new PullUpModeStrength();

        assertDoesNotThrow(
                () -> strength.setFourToSix(record)
        );
    }
    @Test
    void fourToSixShouldRejectInvalidReps() {

        PullUpRecord record = new PullUpRecord();
        record.setExternalWeight(10);
        record.setReps(8);
        record.setRir(1);

        PullUpModeStrength strength = new PullUpModeStrength();

        assertThrows(
                IllegalArgumentException.class,
                () -> strength.setFourToSix(record)
        );
    }
        @Test
        void tenToTwelveShouldAcceptValidReps() {

            PullUpRecord record = new PullUpRecord();
            record.setExternalWeight(10);
            record.setReps(11);
            record.setRir(1);

            PullUpModeStrength strength = new PullUpModeStrength();

            assertDoesNotThrow(
                    () -> strength.setTenToTwelve(record)
            );
        }
    @Test
    void tenToTwelveShouldRejectInvalidReps() {

        PullUpRecord record = new PullUpRecord();
        record.setExternalWeight(10);
        record.setReps(8);
        record.setRir(1);

        PullUpModeStrength strength = new PullUpModeStrength();

        assertThrows(
                IllegalArgumentException.class,
                () -> strength.setTenToTwelve(record)
        );
    }
}
