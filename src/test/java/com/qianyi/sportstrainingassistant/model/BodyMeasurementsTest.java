package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BodyMeasurementsTest {


    // ==================== Chest ====================

    @Test
    void chestShouldAcceptValidValue() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setChestCircumferenceCm(100.0);

        assertEquals(
                100.0,
                measurements.getChestCircumferenceCm(),
                0.001
        );
    }

    @Test
    void chestShouldAcceptBoundaryValues() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setChestCircumferenceCm(40.0);
        assertEquals(
                40.0,
                measurements.getChestCircumferenceCm(),
                0.001
        );

        measurements.setChestCircumferenceCm(200.0);
        assertEquals(
                200.0,
                measurements.getChestCircumferenceCm(),
                0.001
        );
    }

    @Test
    void chestShouldRejectValueBelowMinimum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setChestCircumferenceCm(39.9)
        );
    }

    @Test
    void chestShouldRejectValueAboveMaximum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setChestCircumferenceCm(200.1)
        );
    }

    @Test
    void chestShouldRejectNaN() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setChestCircumferenceCm(Double.NaN)
        );
    }


    // ==================== Waist ====================

    @Test
    void waistShouldAcceptValidValue() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setWaistCircumferenceCm(80.0);

        assertEquals(
                80.0,
                measurements.getWaistCircumferenceCm(),
                0.001
        );
    }

    @Test
    void waistShouldAcceptBoundaryValues() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setWaistCircumferenceCm(30.0);
        assertEquals(
                30.0,
                measurements.getWaistCircumferenceCm(),
                0.001
        );

        measurements.setWaistCircumferenceCm(200.0);
        assertEquals(
                200.0,
                measurements.getWaistCircumferenceCm(),
                0.001
        );
    }

    @Test
    void waistShouldRejectValueBelowMinimum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setWaistCircumferenceCm(29.9)
        );
    }

    @Test
    void waistShouldRejectValueAboveMaximum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setWaistCircumferenceCm(200.1)
        );
    }

    @Test
    void waistShouldRejectNaN() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setWaistCircumferenceCm(Double.NaN)
        );
    }


    // ==================== Arm ====================


    @Test
    void shouldStoreLeftAndRightArmSeparately() {
        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setLeftArmCircumferenceCm(34.5);
        measurements.setRightArmCircumferenceCm(35.0);

        assertEquals(34.5, measurements.getLeftArmCircumferenceCm());
        assertEquals(35.0, measurements.getRightArmCircumferenceCm());
    }

    @Test
    void shouldRejectInvalidArmCircumference() {
        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(IllegalArgumentException.class,
                () -> measurements.setLeftArmCircumferenceCm(85.0));

        assertThrows(IllegalArgumentException.class,
                () -> measurements.setRightArmCircumferenceCm(5.0));
    }

    @Test
    void shouldAllowMissingArmMeasurements() {
        BodyMeasurements measurements = new BodyMeasurements();

        assertNull(measurements.getLeftArmCircumferenceCm());
        assertNull(measurements.getRightArmCircumferenceCm());
    }



    // ==================== Thigh ====================

    @Test
    void thighShouldAcceptValidValue() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setThighCircumferenceCm(60.0);

        assertEquals(
                60.0,
                measurements.getThighCircumferenceCm(),
                0.001
        );
    }

    @Test
    void thighShouldAcceptBoundaryValues() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setThighCircumferenceCm(20.0);
        assertEquals(
                20.0,
                measurements.getThighCircumferenceCm(),
                0.001
        );

        measurements.setThighCircumferenceCm(120.0);
        assertEquals(
                120.0,
                measurements.getThighCircumferenceCm(),
                0.001
        );
    }

    @Test
    void thighShouldRejectValueBelowMinimum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setThighCircumferenceCm(19.9)
        );
    }

    @Test
    void thighShouldRejectValueAboveMaximum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setThighCircumferenceCm(120.1)
        );
    }

    @Test
    void thighShouldRejectNaN() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setThighCircumferenceCm(Double.NaN)
        );
    }



    // ==================== Optional Values ====================

    @Test
    void allMeasurementsShouldAllowNull() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertDoesNotThrow(
                () -> measurements.setChestCircumferenceCm(null)
        );

        assertDoesNotThrow(
                () -> measurements.setWaistCircumferenceCm(null)
        );

        assertDoesNotThrow(
                () -> measurements.setLeftArmCircumferenceCm(null)
        );

        assertDoesNotThrow(
                () -> measurements.setRightArmCircumferenceCm(null)
        );

        assertDoesNotThrow(
                () -> measurements.setThighCircumferenceCm(null)
        );

        assertNull(measurements.getChestCircumferenceCm());
        assertNull(measurements.getWaistCircumferenceCm());
        assertNull(measurements.getLeftArmCircumferenceCm());
        assertNull(measurements.getRightArmCircumferenceCm());
        assertNull(measurements.getThighCircumferenceCm());
    }

}