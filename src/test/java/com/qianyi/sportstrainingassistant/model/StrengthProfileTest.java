package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StrengthProfileTest {


    private ExerciseStrength createExerciseStrength() {

        StrengthRecord record =
                new StrengthRecord(
                        80.0,
                        5,
                        1
                );

        return new ExerciseStrength(
                List.of(record)
        );
    }


    private PullUpStrength createPullUpStrength() {

        PullUpRecord record =
                new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        20.0,
                        5,
                        1
                );

        return new PullUpStrength(
                List.of(record)
        );
    }


    // ==================== Chest ====================

    @Test
    void shouldStoreAndRetrieveBenchPress() {

        ExerciseStrength strength =
                createExerciseStrength();

        StrengthProfile profile =
                new StrengthProfile();

        profile.setBenchPress(strength);

        assertSame(
                strength,
                profile.getBenchPress()
        );
    }


    // ==================== Back ====================

    @Test
    void shouldStoreAndRetrieveBarbellRow() {

        ExerciseStrength strength =
                createExerciseStrength();

        StrengthProfile profile =
                new StrengthProfile();

        profile.setBarbellRow(strength);

        assertSame(
                strength,
                profile.getBarbellRow()
        );
    }


    @Test
    void shouldStoreAndRetrievePullUp() {

        PullUpStrength strength =
                createPullUpStrength();

        StrengthProfile profile =
                new StrengthProfile();

        profile.setPullUp(strength);

        assertSame(
                strength,
                profile.getPullUp()
        );
    }


    // ==================== Shoulder ====================

    @Test
    void shouldStoreAndRetrieveStrictPress() {

        ExerciseStrength strength =
                createExerciseStrength();

        StrengthProfile profile =
                new StrengthProfile();

        profile.setStrictPress(strength);

        assertSame(
                strength,
                profile.getStrictPress()
        );
    }


    @Test
    void shouldStoreAndRetrieveDumbbellShoulderPress() {

        ExerciseStrength strength =
                createExerciseStrength();

        StrengthProfile profile =
                new StrengthProfile();

        profile.setDumbbellShoulderPress(strength);

        assertSame(
                strength,
                profile.getDumbbellShoulderPress()
        );
    }


    // ==================== Legs ====================

    @Test
    void shouldStoreAndRetrieveSquat() {

        ExerciseStrength strength =
                createExerciseStrength();

        StrengthProfile profile =
                new StrengthProfile();

        profile.setSquat(strength);

        assertSame(
                strength,
                profile.getSquat()
        );
    }


    @Test
    void shouldStoreAndRetrieveLegPress() {

        ExerciseStrength strength =
                createExerciseStrength();

        StrengthProfile profile =
                new StrengthProfile();

        profile.setLegPress(strength);

        assertSame(
                strength,
                profile.getLegPress()
        );
    }


    // ==================== Optional Data ====================

    @Test
    void strengthFieldsShouldBeOptional() {

        StrengthProfile profile =
                new StrengthProfile();

        assertNull(profile.getBenchPress());
        assertNull(profile.getBarbellRow());
        assertNull(profile.getPullUp());
        assertNull(profile.getStrictPress());
        assertNull(profile.getDumbbellShoulderPress());
        assertNull(profile.getSquat());
        assertNull(profile.getLegPress());
    }
}