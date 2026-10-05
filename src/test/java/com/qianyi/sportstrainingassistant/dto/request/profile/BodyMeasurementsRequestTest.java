package com.qianyi.sportstrainingassistant.dto.request.profile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BodyMeasurementsRequestTest {

    private static Validator validator;


    @BeforeAll
    static void setUpValidator() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }


    // ==================== Valid Request ====================

    @Test
    void shouldAcceptAllNullMeasurements() {

        BodyMeasurementsRequest request =
                new BodyMeasurementsRequest();

        Set<ConstraintViolation<BodyMeasurementsRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    @Test
    void shouldAcceptValidMeasurements() {

        BodyMeasurementsRequest request =
                createValidRequest();

        Set<ConstraintViolation<BodyMeasurementsRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Chest ====================

    @Test
    void shouldAcceptChestBoundaryValues() {

        BodyMeasurementsRequest minRequest =
                createValidRequest();
        minRequest.setChestCircumferenceCm(40.0);

        BodyMeasurementsRequest maxRequest =
                createValidRequest();
        maxRequest.setChestCircumferenceCm(200.0);

        assertNoViolationFor(
                minRequest,
                "chestCircumferenceCm"
        );

        assertNoViolationFor(
                maxRequest,
                "chestCircumferenceCm"
        );
    }


    @Test
    void shouldRejectChestBelowMinimum() {

        BodyMeasurementsRequest request =
                createValidRequest();

        request.setChestCircumferenceCm(39.9);

        assertHasViolationFor(
                request,
                "chestCircumferenceCm"
        );
    }


    @Test
    void shouldRejectChestAboveMaximum() {

        BodyMeasurementsRequest request =
                createValidRequest();

        request.setChestCircumferenceCm(200.1);

        assertHasViolationFor(
                request,
                "chestCircumferenceCm"
        );
    }


    // ==================== Waist ====================

    @Test
    void shouldAcceptWaistBoundaryValues() {

        BodyMeasurementsRequest minRequest =
                createValidRequest();
        minRequest.setWaistCircumferenceCm(30.0);

        BodyMeasurementsRequest maxRequest =
                createValidRequest();
        maxRequest.setWaistCircumferenceCm(200.0);

        assertNoViolationFor(
                minRequest,
                "waistCircumferenceCm"
        );

        assertNoViolationFor(
                maxRequest,
                "waistCircumferenceCm"
        );
    }


    @Test
    void shouldRejectWaistBelowMinimum() {

        BodyMeasurementsRequest request =
                createValidRequest();

        request.setWaistCircumferenceCm(29.9);

        assertHasViolationFor(
                request,
                "waistCircumferenceCm"
        );
    }


    @Test
    void shouldRejectWaistAboveMaximum() {

        BodyMeasurementsRequest request =
                createValidRequest();

        request.setWaistCircumferenceCm(200.1);

        assertHasViolationFor(
                request,
                "waistCircumferenceCm"
        );
    }


    // ==================== Arm ====================

    @Test
    void shouldAcceptArmBoundaryValues() {

        BodyMeasurementsRequest minRequest =
                createValidRequest();
        minRequest.setArmCircumferenceCm(10.0);

        BodyMeasurementsRequest maxRequest =
                createValidRequest();
        maxRequest.setArmCircumferenceCm(80.0);

        assertNoViolationFor(
                minRequest,
                "armCircumferenceCm"
        );

        assertNoViolationFor(
                maxRequest,
                "armCircumferenceCm"
        );
    }


    @Test
    void shouldRejectArmBelowMinimum() {

        BodyMeasurementsRequest request =
                createValidRequest();

        request.setArmCircumferenceCm(9.9);

        assertHasViolationFor(
                request,
                "armCircumferenceCm"
        );
    }


    @Test
    void shouldRejectArmAboveMaximum() {

        BodyMeasurementsRequest request =
                createValidRequest();

        request.setArmCircumferenceCm(80.1);

        assertHasViolationFor(
                request,
                "armCircumferenceCm"
        );
    }


    // ==================== Thigh ====================

    @Test
    void shouldAcceptThighBoundaryValues() {

        BodyMeasurementsRequest minRequest =
                createValidRequest();
        minRequest.setThighCircumferenceCm(20.0);

        BodyMeasurementsRequest maxRequest =
                createValidRequest();
        maxRequest.setThighCircumferenceCm(120.0);

        assertNoViolationFor(
                minRequest,
                "thighCircumferenceCm"
        );

        assertNoViolationFor(
                maxRequest,
                "thighCircumferenceCm"
        );
    }


    @Test
    void shouldRejectThighBelowMinimum() {

        BodyMeasurementsRequest request =
                createValidRequest();

        request.setThighCircumferenceCm(19.9);

        assertHasViolationFor(
                request,
                "thighCircumferenceCm"
        );
    }


    @Test
    void shouldRejectThighAboveMaximum() {

        BodyMeasurementsRequest request =
                createValidRequest();

        request.setThighCircumferenceCm(120.1);

        assertHasViolationFor(
                request,
                "thighCircumferenceCm"
        );
    }


    // ==================== Helpers ====================

    private BodyMeasurementsRequest createValidRequest() {

        BodyMeasurementsRequest request =
                new BodyMeasurementsRequest();

        request.setChestCircumferenceCm(100.0);
        request.setWaistCircumferenceCm(80.0);
        request.setArmCircumferenceCm(35.0);
        request.setThighCircumferenceCm(55.0);

        return request;
    }


    private void assertHasViolationFor(
            BodyMeasurementsRequest request,
            String propertyName) {

        Set<ConstraintViolation<BodyMeasurementsRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .equals(propertyName));

        assertTrue(
                found,
                "Expected validation violation for " + propertyName
        );
    }


    private void assertNoViolationFor(
            BodyMeasurementsRequest request,
            String propertyName) {

        Set<ConstraintViolation<BodyMeasurementsRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .equals(propertyName));

        assertFalse(
                found,
                "Did not expect validation violation for " + propertyName
        );
    }
}