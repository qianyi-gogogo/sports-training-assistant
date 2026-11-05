
package com.qianyi.sportstrainingassistant.entity;

import com.qianyi.sportstrainingassistant.model.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BodyProfileEntityTest {

    private BodyProfile createProfile() {

        BasicBodyInfo basic = new BasicBodyInfo(
                175.0,
                70.0,
                21,
                BiologicalSex.MALE
        );

        basic.setBodyFatPercentage(15.0);

        BodyMeasurements measurements =
                new BodyMeasurements();

        measurements.setChestCircumferenceCm(100.0);
        measurements.setWaistCircumferenceCm(78.0);
        measurements.setLeftArmCircumferenceCm(34.5);
        measurements.setRightArmCircumferenceCm(35.0);
        measurements.setThighCircumferenceCm(58.0);

        BodyProfile profile = new BodyProfile(basic);
        profile.setBodyMeasurements(measurements);

        return profile;
    }


    // ==================== Create ====================

    @Test
    void shouldCreateBodyProfileEntity() {

        BodyProfileEntity entity =
                new BodyProfileEntity(1L, createProfile());

        assertEquals(1L, entity.getUserId());

        BodyProfile restored = entity.toModel();

        assertEquals(
                175.0,
                restored.getBasicBodyInfo().getHeightCm()
        );

        assertEquals(
                70.0,
                restored.getBasicBodyInfo().getWeightKg()
        );

        assertEquals(
                34.5,
                restored.getBodyMeasurements()
                        .getLeftArmCircumferenceCm()
        );

        assertEquals(
                35.0,
                restored.getBodyMeasurements()
                        .getRightArmCircumferenceCm()
        );
    }


    // ==================== Update ====================

    @Test
    void shouldUpdateCurrentBodyProfile() {

        BodyProfileEntity entity =
                new BodyProfileEntity(1L, createProfile());

        BodyProfile updatedProfile = createProfile();

        updatedProfile.getBasicBodyInfo()
                .setWeightKg(72.5);

        updatedProfile.getBodyMeasurements()
                .setWaistCircumferenceCm(76.0);

        entity.updateFromModel(updatedProfile);

        BodyProfile restored = entity.toModel();

        assertEquals(
                72.5,
                restored.getBasicBodyInfo().getWeightKg()
        );

        assertEquals(
                76.0,
                restored.getBodyMeasurements()
                        .getWaistCircumferenceCm()
        );

        assertEquals(1L, entity.getUserId());
    }


    // ==================== Validation ====================

    @Test
    void shouldRejectInvalidBodyProfile() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BodyProfileEntity(
                        null,
                        createProfile()
                )
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new BodyProfileEntity(
                        1L,
                        null
                )
        );
    }
}
