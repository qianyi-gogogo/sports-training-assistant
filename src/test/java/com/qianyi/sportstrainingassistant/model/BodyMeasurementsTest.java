package com.qianyi.sportstrainingassistant.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class BodyMeasurementsTest {
    // ==================== Chest Circumference ====================

    @Test
    void shouldSetValidChestCircumference() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setChestCircumference(100.0);

        assertEquals(100.0, measurements.getChestCircumference(), 0.001);
    }

    @Test
    void shouldAcceptChestCircumferenceBoundaryValues() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setChestCircumference(40.0);
        assertEquals(40.0, measurements.getChestCircumference(), 0.001);

        measurements.setChestCircumference(200.0);
        assertEquals(200.0, measurements.getChestCircumference(), 0.001);
    }

    @Test
    void shouldRejectChestCircumferenceBelowMinimum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setChestCircumference(39.9)
        );
    }

    @Test
    void shouldRejectChestCircumferenceAboveMaximum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setChestCircumference(200.1)
        );
    }


    // ==================== Waist Circumference ====================

    @Test
    void shouldSetValidWaistCircumference() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setWaistCircumference(80.0);

        assertEquals(80.0, measurements.getWaistCircumference(), 0.001);
    }

    @Test
    void shouldAcceptWaistCircumferenceBoundaryValues() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setWaistCircumference(30.0);
        assertEquals(30.0, measurements.getWaistCircumference(), 0.001);

        measurements.setWaistCircumference(200.0);
        assertEquals(200.0, measurements.getWaistCircumference(), 0.001);
    }

    @Test
    void shouldRejectWaistCircumferenceBelowMinimum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setWaistCircumference(29.9)
        );
    }

    @Test
    void shouldRejectWaistCircumferenceAboveMaximum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setWaistCircumference(200.1)
        );
    }


    // ==================== Arm Circumference ====================

    @Test
    void shouldSetValidArmCircumference() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setArmCircumference(35.0);

        assertEquals(35.0, measurements.getArmCircumference(), 0.001);
    }

    @Test
    void shouldAcceptArmCircumferenceBoundaryValues() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setArmCircumference(10.0);
        assertEquals(10.0, measurements.getArmCircumference(), 0.001);

        measurements.setArmCircumference(80.0);
        assertEquals(80.0, measurements.getArmCircumference(), 0.001);
    }

    @Test
    void shouldRejectArmCircumferenceBelowMinimum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setArmCircumference(9.9)
        );
    }

    @Test
    void shouldRejectArmCircumferenceAboveMaximum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setArmCircumference(80.1)
        );
    }


    // ==================== Thigh Circumference ====================

    @Test
    void shouldSetValidThighCircumference() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setThighCircumference(60.0);

        assertEquals(60.0, measurements.getThighCircumference(), 0.001);
    }

    @Test
    void shouldAcceptThighCircumferenceBoundaryValues() {

        BodyMeasurements measurements = new BodyMeasurements();

        measurements.setThighCircumference(20.0);
        assertEquals(20.0, measurements.getThighCircumference(), 0.001);

        measurements.setThighCircumference(120.0);
        assertEquals(120.0, measurements.getThighCircumference(), 0.001);
    }

    @Test
    void shouldRejectThighCircumferenceBelowMinimum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setThighCircumference(19.9)
        );
    }

    @Test
    void shouldRejectThighCircumferenceAboveMaximum() {

        BodyMeasurements measurements = new BodyMeasurements();

        assertThrows(
                IllegalArgumentException.class,
                () -> measurements.setThighCircumference(120.1)
        );
    }
}
