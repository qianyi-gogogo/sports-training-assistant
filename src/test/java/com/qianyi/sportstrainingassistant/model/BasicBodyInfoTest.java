package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BasicBodyInfoTest {


    // ==================== Valid Creation ====================

    @Test
    void shouldCreateValidBasicBodyInfo() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        assertEquals(175.0, info.getHeightCm(), 0.001);
        assertEquals(70.0, info.getWeightKg(), 0.001);
        assertEquals(20, info.getAge());
        assertEquals(BiologicalSex.MALE, info.getBiologicalSex());

        // Body fat is optional
        assertNull(info.getBodyFatPercentage());
    }


    // ==================== Height ====================

    @Test
    void shouldAcceptHeightBoundaryValues() {

        BasicBodyInfo minimum =
                new BasicBodyInfo(
                        50.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        BasicBodyInfo maximum =
                new BasicBodyInfo(
                        250.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        assertEquals(50.0, minimum.getHeightCm(), 0.001);
        assertEquals(250.0, maximum.getHeightCm(), 0.001);
    }

    @Test
    void shouldRejectHeightBelowMinimum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicBodyInfo(
                        49.9,
                        70.0,
                        20,
                        BiologicalSex.MALE
                )
        );
    }

    @Test
    void shouldRejectHeightAboveMaximum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicBodyInfo(
                        250.1,
                        70.0,
                        20,
                        BiologicalSex.MALE
                )
        );
    }

    @Test
    void shouldRejectNaNHeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicBodyInfo(
                        Double.NaN,
                        70.0,
                        20,
                        BiologicalSex.MALE
                )
        );
    }


    // ==================== Weight ====================

    @Test
    void shouldAcceptWeightBoundaryValues() {

        BasicBodyInfo minimum =
                new BasicBodyInfo(
                        175.0,
                        10.0,
                        20,
                        BiologicalSex.MALE
                );

        BasicBodyInfo maximum =
                new BasicBodyInfo(
                        175.0,
                        300.0,
                        20,
                        BiologicalSex.MALE
                );

        assertEquals(10.0, minimum.getWeightKg(), 0.001);
        assertEquals(300.0, maximum.getWeightKg(), 0.001);
    }

    @Test
    void shouldRejectWeightBelowMinimum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicBodyInfo(
                        175.0,
                        9.9,
                        20,
                        BiologicalSex.MALE
                )
        );
    }

    @Test
    void shouldRejectWeightAboveMaximum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicBodyInfo(
                        175.0,
                        300.1,
                        20,
                        BiologicalSex.MALE
                )
        );
    }

    @Test
    void shouldRejectNaNWeight() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicBodyInfo(
                        175.0,
                        Double.NaN,
                        20,
                        BiologicalSex.MALE
                )
        );
    }


    // ==================== Body Fat Percentage ====================

    @Test
    void shouldAcceptValidBodyFatPercentage() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        info.setBodyFatPercentage(15.0);

        assertEquals(
                15.0,
                info.getBodyFatPercentage(),
                0.001
        );
    }

    @Test
    void shouldAcceptNullBodyFatPercentage() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        info.setBodyFatPercentage(null);

        assertNull(info.getBodyFatPercentage());
    }

    @Test
    void shouldAcceptBodyFatBoundaryValues() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        info.setBodyFatPercentage(3.0);
        assertEquals(3.0, info.getBodyFatPercentage(), 0.001);

        info.setBodyFatPercentage(60.0);
        assertEquals(60.0, info.getBodyFatPercentage(), 0.001);
    }

    @Test
    void shouldRejectBodyFatBelowMinimum() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setBodyFatPercentage(2.9)
        );
    }

    @Test
    void shouldRejectBodyFatAboveMaximum() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setBodyFatPercentage(60.1)
        );
    }

    @Test
    void shouldRejectNaNBodyFatPercentage() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setBodyFatPercentage(Double.NaN)
        );
    }


    // ==================== Age ====================

    @Test
    void shouldAcceptAgeBoundaryValues() {

        BasicBodyInfo minimum =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        8,
                        BiologicalSex.MALE
                );

        BasicBodyInfo maximum =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        100,
                        BiologicalSex.MALE
                );

        assertEquals(8, minimum.getAge());
        assertEquals(100, maximum.getAge());
    }

    @Test
    void shouldRejectAgeBelowMinimum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicBodyInfo(
                        175.0,
                        70.0,
                        7,
                        BiologicalSex.MALE
                )
        );
    }

    @Test
    void shouldRejectAgeAboveMaximum() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicBodyInfo(
                        175.0,
                        70.0,
                        101,
                        BiologicalSex.MALE
                )
        );
    }


    // ==================== Biological Sex ====================

    @Test
    void shouldAcceptMaleBiologicalSex() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        assertEquals(
                BiologicalSex.MALE,
                info.getBiologicalSex()
        );
    }

    @Test
    void shouldAcceptFemaleBiologicalSex() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.FEMALE
                );

        assertEquals(
                BiologicalSex.FEMALE,
                info.getBiologicalSex()
        );
    }

    @Test
    void shouldRejectNullBiologicalSex() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        null
                )
        );
    }


    // ==================== Updating Values ====================

    @Test
    void shouldAllowValidWeightUpdate() {

        BasicBodyInfo info =
                new BasicBodyInfo(
                        175.0,
                        70.0,
                        20,
                        BiologicalSex.MALE
                );

        info.setWeightKg(72.5);

        assertEquals(72.5, info.getWeightKg(), 0.001);
    }
}