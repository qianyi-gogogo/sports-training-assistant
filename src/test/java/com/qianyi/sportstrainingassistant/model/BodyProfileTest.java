package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BodyProfileTest {


    private BasicBodyInfo createBasicBodyInfo() {

        return new BasicBodyInfo(
                175.0,
                70.0,
                20,
                BiologicalSex.MALE
        );
    }


    // ==================== Valid Body Profile ====================

    @Test
    void shouldCreateBodyProfileWithBasicBodyInfo() {

        BasicBodyInfo basicBodyInfo =
                createBasicBodyInfo();

        BodyProfile profile =
                new BodyProfile(basicBodyInfo);

        assertSame(
                basicBodyInfo,
                profile.getBasicBodyInfo()
        );
    }


    // ==================== Basic Body Info ====================

    @Test
    void shouldRejectNullBasicBodyInfoWhenCreatingProfile() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BodyProfile(null)
        );
    }


    @Test
    void shouldAllowUpdatingBasicBodyInfo() {

        BodyProfile profile =
                new BodyProfile(
                        createBasicBodyInfo()
                );

        BasicBodyInfo updatedInfo =
                new BasicBodyInfo(
                        180.0,
                        75.0,
                        21,
                        BiologicalSex.MALE
                );

        profile.setBasicBodyInfo(updatedInfo);

        assertSame(
                updatedInfo,
                profile.getBasicBodyInfo()
        );
    }


    @Test
    void shouldRejectSettingBasicBodyInfoToNull() {

        BodyProfile profile =
                new BodyProfile(
                        createBasicBodyInfo()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> profile.setBasicBodyInfo(null)
        );
    }


    // ==================== Body Measurements ====================

    @Test
    void bodyMeasurementsShouldBeOptional() {

        BodyProfile profile =
                new BodyProfile(
                        createBasicBodyInfo()
                );

        assertNull(
                profile.getBodyMeasurements()
        );
    }


    @Test
    void shouldStoreBodyMeasurements() {

        BodyProfile profile =
                new BodyProfile(
                        createBasicBodyInfo()
                );

        BodyMeasurements measurements =
                new BodyMeasurements();

        measurements.setChestCircumferenceCm(100.0);
        measurements.setWaistCircumferenceCm(80.0);

        profile.setBodyMeasurements(measurements);

        assertSame(
                measurements,
                profile.getBodyMeasurements()
        );
    }


    @Test
    void shouldAllowRemovingBodyMeasurements() {

        BodyProfile profile =
                new BodyProfile(
                        createBasicBodyInfo()
                );

        BodyMeasurements measurements =
                new BodyMeasurements();

        profile.setBodyMeasurements(measurements);

        profile.setBodyMeasurements(null);

        assertNull(
                profile.getBodyMeasurements()
        );
    }


    // ==================== Strength Profile ====================

    @Test
    void strengthProfileShouldBeOptional() {

        BodyProfile profile =
                new BodyProfile(
                        createBasicBodyInfo()
                );

        assertNull(
                profile.getStrengthProfile()
        );
    }


    @Test
    void shouldStoreStrengthProfile() {

        BodyProfile profile =
                new BodyProfile(
                        createBasicBodyInfo()
                );

        StrengthRecord record =
                new StrengthRecord(
                        80.0,
                        5,
                        1
                );

        ExerciseStrength benchPress =
                new ExerciseStrength(
                        List.of(record)
                );

        StrengthProfile strengthProfile =
                new StrengthProfile();

        strengthProfile.setBenchPress(benchPress);

        profile.setStrengthProfile(strengthProfile);

        assertSame(
                strengthProfile,
                profile.getStrengthProfile()
        );

        assertSame(
                benchPress,
                profile
                        .getStrengthProfile()
                        .getBenchPress()
        );
    }


    @Test
    void shouldAllowRemovingStrengthProfile() {

        BodyProfile profile =
                new BodyProfile(
                        createBasicBodyInfo()
                );

        StrengthProfile strengthProfile =
                new StrengthProfile();

        profile.setStrengthProfile(strengthProfile);

        profile.setStrengthProfile(null);

        assertNull(
                profile.getStrengthProfile()
        );
    }


    // ==================== Complete Example ====================

    @Test
    void shouldCreateCompleteBodyProfile() {

        BasicBodyInfo basicBodyInfo =
                createBasicBodyInfo();

        basicBodyInfo.setBodyFatPercentage(15.0);

        BodyMeasurements measurements =
                new BodyMeasurements();

        measurements.setChestCircumferenceCm(100.0);
        measurements.setWaistCircumferenceCm(80.0);
        measurements.setArmCircumferenceCm(35.0);
        measurements.setThighCircumferenceCm(60.0);

        StrengthRecord benchRecord =
                new StrengthRecord(
                        80.0,
                        5,
                        1
                );

        ExerciseStrength benchStrength =
                new ExerciseStrength(
                        List.of(benchRecord)
                );

        PullUpRecord pullUpRecord =
                new PullUpRecord(
                        PullUpMode.WEIGHTED,
                        20.0,
                        5,
                        1
                );

        PullUpStrength pullUpStrength =
                new PullUpStrength(
                        List.of(pullUpRecord)
                );

        StrengthProfile strengthProfile =
                new StrengthProfile();

        strengthProfile.setBenchPress(benchStrength);
        strengthProfile.setPullUp(pullUpStrength);

        BodyProfile profile =
                new BodyProfile(basicBodyInfo);

        profile.setBodyMeasurements(measurements);
        profile.setStrengthProfile(strengthProfile);

        assertSame(
                basicBodyInfo,
                profile.getBasicBodyInfo()
        );

        assertSame(
                measurements,
                profile.getBodyMeasurements()
        );

        assertSame(
                strengthProfile,
                profile.getStrengthProfile()
        );
    }
}