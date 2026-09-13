package com.qianyi.sportstrainingassistant.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;


public class StrengthProfileTest {

    @Test
    void shouldSetAndGetBenchPress() {
        // ==================== Bench Press ====================

        // Create a bench press strength object
        ExerciseStrength benchPress = new ExerciseStrength();

        // Create StrengthProfile
        StrengthProfile profile = new StrengthProfile();

        // Store bench press strength
        profile.setBenchPress(benchPress);

        // Verify that the same object can be retrieved
        assertSame(benchPress, profile.getBenchPress());
    }
    // ==================== Barbell Row ====================

    @Test
    void shouldSetAndGetBarbellRow() {

        ExerciseStrength barbellRow = new ExerciseStrength();

        StrengthProfile profile = new StrengthProfile();
        profile.setBarbellRow(barbellRow);

        assertSame(barbellRow, profile.getBarbellRow());
    }


    // ==================== Strict Press ====================

    @Test
    void shouldSetAndGetStrictPress() {

        ExerciseStrength strictPress = new ExerciseStrength();

        StrengthProfile profile = new StrengthProfile();
        profile.setStrictPress(strictPress);

        assertSame(strictPress, profile.getStrictPress());
    }


    // ==================== Dumbbell Shoulder Press ====================

    @Test
    void shouldSetAndGetDumbbellShoulderPress() {

        ExerciseStrength dumbbellShoulderPress = new ExerciseStrength();

        StrengthProfile profile = new StrengthProfile();
        profile.setDumbbellShoulderPress(dumbbellShoulderPress);

        assertSame(
                dumbbellShoulderPress,
                profile.getDumbbellShoulderPress()
        );
    }


    // ==================== Squat ====================

    @Test
    void shouldSetAndGetSquat() {

        ExerciseStrength squat = new ExerciseStrength();

        StrengthProfile profile = new StrengthProfile();
        profile.setSquat(squat);

        assertSame(squat, profile.getSquat());
    }


    // ==================== Leg Press ====================

    @Test
    void shouldSetAndGetLegPress() {

        ExerciseStrength legPress = new ExerciseStrength();

        StrengthProfile profile = new StrengthProfile();
        profile.setLegPress(legPress);

        assertSame(legPress, profile.getLegPress());
    }
// ==================== Pull Up ====================

    @Test
    void shouldSetAndGetPullUp() {

        PullUpStrength pullUp = new PullUpStrength();

        StrengthProfile profile = new StrengthProfile();
        profile.setPullUp(pullUp);

        assertSame(pullUp, profile.getPullUp());
    }

}




