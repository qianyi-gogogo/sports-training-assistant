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
    void armShouldAcceptValidValue() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setArmCircumferenceCm(35.0);

        assertEquals(
                35.0,
                measurements.getArmCircumferenceCm(),
                0.001
        );
    }

    @Test
    void armShouldAcceptBoundaryValues() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setArmCircumferenceCm(10.0);
        assertEquals(
                10.0,
                measurements.getArmCircumferenceCm(),
                0.001
        );

        measurements.setArmCircumferenceCm(80.0);
        assertEquals(
                80.0,
                measurements.getArmCircumferenceCm(),
                0.001
        );
    }

    @Test
    void armShouldRejectValueBelowMinimum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setArmCircumferenceCm(9.9)
        );
    }

    @Test
    void armShouldRejectValueAboveMaximum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setArmCircumferenceCm(80.1)
        );
    }

    @Test
    void armShouldRejectNaN() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setArmCircumferenceCm(Double.NaN)
        );
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
                () -> measurements.setArmCircumferenceCm(null)
        );

        assertDoesNotThrow(
                () -> measurements.setThighCircumferenceCm(null)
        );

        assertNull(measurements.getChestCircumferenceCm());
        assertNull(measurements.getWaistCircumferenceCm());
        assertNull(measurements.getArmCircumferenceCm());
        assertNull(measurements.getThighCircumferenceCm());
    }
}