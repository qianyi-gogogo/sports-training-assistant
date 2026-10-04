package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PullUpStrengthTest {


    // ==================== Valid Records ====================

    @Test
    void shouldCreatePullUpStrengthWithOneRecord() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        10,
                        1
                );

        PullUpStrength strength =
                new PullUpStrength(
                        List.of(record)
                );

        assertEquals(1, strength.getRecords().size());
        assertSame(record, strength.getRecords().get(0));
    }


    @Test
    void shouldAcceptDifferentPullUpModes() {

        PullUpRecord bodyweight =
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        12,
                        1
                );

        PullUpRecord weighted =
                new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        20.0,
                        5,
                        1
                );

        PullUpRecord assisted =
                new PullUpRecord(
                        PullUpMode.ASSISTED,
                        25.0,
                        8,
                        2
                );

        PullUpStrength strength =
                new PullUpStrength(
                        List.of(
                                bodyweight,
                                weighted,
                                assisted
                        )
                );

        assertEquals(3, strength.getRecords().size());

        assertEquals(
                PullUpMode.BODYWEIGHT,
                strength.getRecords().get(0).getMode()
        );

        assertEquals(
                PullUpMode.WEIGHTED,
                strength.getRecords().get(1).getMode()
        );

        assertEquals(
                PullUpMode.ASSISTED,
                strength.getRecords().get(2).getMode()
        );
    }


    // ==================== No Strength Evaluation ====================

    @Test
    void shouldAcceptDifferentRepRanges() {

        PullUpRecord record1 =
                new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        25.0,
                        3,
                        1
                );

        PullUpRecord record2 =
                new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        15.0,
                        7,
                        2
                );

        PullUpRecord record3 =
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        15,
                        3
                );

        PullUpStrength strength =
                new PullUpStrength(
                        List.of(
                                record1,
                                record2,
                                record3
                        )
                );

        assertEquals(3, strength.getRecords().size());
    }


    // ==================== Records Validation ====================

    @Test
    void shouldRejectNullRecordsList() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpStrength(null)
        );
    }


    @Test
    void shouldRejectEmptyRecordsList() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpStrength(List.of())
        );
    }


    @Test
    void shouldRejectNullRecord() {

        List<PullUpRecord> records =
                new ArrayList<>();

        records.add(
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        10,
                        1
                )
        );

        records.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> new PullUpStrength(records)
        );
    }


    // ==================== Defensive Copy ====================

    @Test
    void shouldProtectRecordsFromExternalModification() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        20.0,
                        5,
                        1
                );

        List<PullUpRecord> originalRecords =
                new ArrayList<>();

        originalRecords.add(record);

        PullUpStrength strength =
                new PullUpStrength(originalRecords);

        originalRecords.clear();

        assertEquals(1, strength.getRecords().size());
    }


    @Test
    void returnedRecordsShouldNotBeModifiable() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.BODYWEIGHT,
                        0.0,
                        10,
                        1
                );

        PullUpStrength strength =
                new PullUpStrength(
                        List.of(record)
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> strength.getRecords().clear()
        );
    }
}