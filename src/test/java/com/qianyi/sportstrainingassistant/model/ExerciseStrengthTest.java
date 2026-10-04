package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExerciseStrengthTest {


    // ==================== Valid Records ====================

    @Test
    void shouldCreateExerciseStrengthWithOneRecord() {

        StrengthRecord record =
                new StrengthRecord(
                        80.0,
                        5,
                        1
                );

        ExerciseStrength strength =
                new ExerciseStrength(
                        List.of(record)
                );

        assertEquals(1, strength.getRecords().size());
        assertSame(record, strength.getRecords().get(0));
    }


    @Test
    void shouldCreateExerciseStrengthWithMultipleRecords() {

        StrengthRecord record1 =
                new StrengthRecord(
                        80.0,
                        3,
                        1
                );

        StrengthRecord record2 =
                new StrengthRecord(
                        72.5,
                        6,
                        2
                );

        StrengthRecord record3 =
                new StrengthRecord(
                        60.0,
                        12,
                        4
                );

        ExerciseStrength strength =
                new ExerciseStrength(
                        List.of(
                                record1,
                                record2,
                                record3
                        )
                );

        assertEquals(3, strength.getRecords().size());
    }


    // ==================== No Fixed Rep Ranges ====================

    @Test
    void shouldAcceptAnyPositiveRepCount() {

        StrengthRecord record =
                new StrengthRecord(
                        50.0,
                        7,
                        2
                );

        ExerciseStrength strength =
                new ExerciseStrength(
                        List.of(record)
                );

        assertEquals(7, strength.getRecords().get(0).getReps());
    }


    @Test
    void shouldAcceptRecordWithHighRir() {

        StrengthRecord record =
                new StrengthRecord(
                        50.0,
                        10,
                        5
                );

        ExerciseStrength strength =
                new ExerciseStrength(
                        List.of(record)
                );

        assertEquals(5, strength.getRecords().get(0).getRir());
    }


    @Test
    void shouldAcceptRecordWithoutRir() {

        StrengthRecord record =
                new StrengthRecord(
                        50.0,
                        10,
                        null
                );

        ExerciseStrength strength =
                new ExerciseStrength(
                        List.of(record)
                );

        assertNull(
                strength.getRecords().get(0).getRir()
        );
    }


    // ==================== Records Validation ====================

    @Test
    void shouldRejectNullRecordsList() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExerciseStrength(null)
        );
    }


    @Test
    void shouldRejectEmptyRecordsList() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExerciseStrength(List.of())
        );
    }


    @Test
    void shouldRejectNullRecord() {

        List<StrengthRecord> records =
                new ArrayList<>();

        records.add(
                new StrengthRecord(
                        80.0,
                        5,
                        1
                )
        );

        records.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> new ExerciseStrength(records)
        );
    }


    // ==================== Defensive Copy ====================

    @Test
    void shouldProtectRecordsFromExternalModification() {

        StrengthRecord record =
                new StrengthRecord(
                        80.0,
                        5,
                        1
                );

        List<StrengthRecord> originalRecords =
                new ArrayList<>();

        originalRecords.add(record);

        ExerciseStrength strength =
                new ExerciseStrength(originalRecords);

        originalRecords.clear();

        assertEquals(1, strength.getRecords().size());
    }


    @Test
    void returnedRecordsShouldNotBeModifiable() {

        StrengthRecord record =
                new StrengthRecord(
                        80.0,
                        5,
                        1
                );

        ExerciseStrength strength =
                new ExerciseStrength(
                        List.of(record)
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> strength.getRecords().clear()
        );
    }
}