package com.qianyi.sportstrainingassistant.model;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class BasicBodyInfoTest {
    // ==================== Height ====================

    @Test
    void shouldSetValidHeight() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setHeight(175.0);

        assertEquals(175.0, info.getHeight(), 0.001);
    }

    @Test
    void shouldAcceptHeightBoundaryValues() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setHeight(50.0);
        assertEquals(50.0, info.getHeight(), 0.001);

        info.setHeight(250.0);
        assertEquals(250.0, info.getHeight(), 0.001);
    }

    @Test
    void shouldRejectHeightBelowMinimum() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setHeight(49.9)
        );
    }

    @Test
    void shouldRejectHeightAboveMaximum() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setHeight(250.1)
        );
    }


    // ==================== Weight ====================

    @Test
    void shouldSetValidWeight() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setWeight(70.0);

        assertEquals(70.0, info.getWeight(), 0.001);
    }

    @Test
    void shouldAcceptWeightBoundaryValues() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setWeight(10.0);
        assertEquals(10.0, info.getWeight(), 0.001);

        info.setWeight(300.0);
        assertEquals(300.0, info.getWeight(), 0.001);
    }

    @Test
    void shouldRejectWeightBelowMinimum() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setWeight(9.9)
        );
    }

    @Test
    void shouldRejectWeightAboveMaximum() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setWeight(300.1)
        );
    }


    // ==================== Body Fat Percentage ====================

    @Test
    void shouldSetValidBodyFatPercentage() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setBodyFatPercentage(15.0);

        assertEquals(15.0, info.getBodyFatPercentage(), 0.001);
    }

    @Test
    void shouldAcceptBodyFatPercentageBoundaryValues() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setBodyFatPercentage(3.0);
        assertEquals(3.0, info.getBodyFatPercentage(), 0.001);

        info.setBodyFatPercentage(60.0);
        assertEquals(60.0, info.getBodyFatPercentage(), 0.001);
    }

    @Test
    void shouldRejectBodyFatPercentageBelowMinimum() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setBodyFatPercentage(2.9)
        );
    }

    @Test
    void shouldRejectBodyFatPercentageAboveMaximum() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setBodyFatPercentage(60.1)
        );
    }


    // ==================== Age ====================

    @Test
    void shouldSetValidAge() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setAge(20);

        assertEquals(20, info.getAge());
    }

    @Test
    void shouldAcceptAgeBoundaryValues() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setAge(8);
        assertEquals(8, info.getAge());

        info.setAge(100);
        assertEquals(100, info.getAge());
    }

    @Test
    void shouldRejectAgeBelowMinimum() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setAge(7)
        );
    }

    @Test
    void shouldRejectAgeAboveMaximum() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setAge(101)
        );
    }


    // ==================== Biological Sex ====================

    @Test
    void shouldSetMaleBiologicalSex() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setBiologicalSex("male");

        assertEquals("male", info.getBiologicalSex());
    }

    @Test
    void shouldSetFemaleBiologicalSex() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setBiologicalSex("female");

        assertEquals("female", info.getBiologicalSex());
    }

    @Test
    void shouldAcceptBiologicalSexIgnoringCase() {

        BasicBodyInfo info = new BasicBodyInfo();

        info.setBiologicalSex("MALE");

        assertEquals("male", info.getBiologicalSex());

        info.setBiologicalSex("Female");

        assertEquals("female", info.getBiologicalSex());
    }

    @Test
    void shouldRejectNullBiologicalSex() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setBiologicalSex(null)
        );
    }

    @Test
    void shouldRejectInvalidBiologicalSex() {

        BasicBodyInfo info = new BasicBodyInfo();

        assertThrows(
                IllegalArgumentException.class,
                () -> info.setBiologicalSex("other")
        );
    }

}
