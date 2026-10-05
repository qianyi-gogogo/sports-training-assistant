package com.qianyi.sportstrainingassistant.dto.request.profile;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StrengthRecordRequestTest {

    private static Validator validator;


    @BeforeAll
    static void setUpValidator() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }


    // ==================== Valid Request ====================

    @Test
    void shouldAcceptValidRequest() {

        StrengthRecordRequest request =
                createValidRequest();

        Set<ConstraintViolation<StrengthRecordRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Weight ====================

    @Test
    void shouldRejectNullWeight() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setWeightKg(null);

        assertHasViolationFor(
                request,
                "weightKg"
        );
    }


    @Test
    void shouldAcceptWeightBoundaryValues() {

        StrengthRecordRequest minRequest =
                createValidRequest();

        minRequest.setWeightKg(0.0);

        StrengthRecordRequest maxRequest =
                createValidRequest();

        maxRequest.setWeightKg(500.0);

        assertNoViolationFor(
                minRequest,
                "weightKg"
        );

        assertNoViolationFor(
                maxRequest,
                "weightKg"
        );
    }


    @Test
    void shouldRejectWeightBelowMinimum() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setWeightKg(-0.1);

        assertHasViolationFor(
                request,
                "weightKg"
        );
    }


    @Test
    void shouldRejectWeightAboveMaximum() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setWeightKg(500.1);

        assertHasViolationFor(
                request,
                "weightKg"
        );
    }


    // ==================== Reps ====================

    @Test
    void shouldRejectNullReps() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setReps(null);

        assertHasViolationFor(
                request,
                "reps"
        );
    }


    @Test
    void shouldAcceptPositiveReps() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setReps(1);

        assertNoViolationFor(
                request,
                "reps"
        );
    }


    @Test
    void shouldRejectZeroReps() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setReps(0);

        assertHasViolationFor(
                request,
                "reps"
        );
    }


    @Test
    void shouldRejectNegativeReps() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setReps(-1);

        assertHasViolationFor(
                request,
                "reps"
        );
    }


    // ==================== RIR ====================

    @Test
    void shouldAcceptNullRir() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setRir(null);

        assertNoViolationFor(
                request,
                "rir"
        );
    }


    @Test
    void shouldAcceptRirBoundaryValues() {

        StrengthRecordRequest zeroRequest =
                createValidRequest();

        zeroRequest.setRir(0);

        StrengthRecordRequest fiveRequest =
                createValidRequest();

        fiveRequest.setRir(5);

        assertNoViolationFor(
                zeroRequest,
                "rir"
        );

        assertNoViolationFor(
                fiveRequest,
                "rir"
        );
    }


    @Test
    void shouldRejectNegativeRir() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setRir(-1);

        assertHasViolationFor(
                request,
                "rir"
        );
    }


    @Test
    void shouldRejectRirAboveFive() {

        StrengthRecordRequest request =
                createValidRequest();

        request.setRir(6);

        assertHasViolationFor(
                request,
                "rir"
        );
    }


    // ==================== Helpers ====================

    private StrengthRecordRequest createValidRequest() {

        StrengthRecordRequest request =
                new StrengthRecordRequest();

        request.setWeightKg(100.0);
        request.setReps(5);
        request.setRir(1);

        return request;
    }


    private void assertHasViolationFor(
            StrengthRecordRequest request,
            String propertyName) {

        Set<ConstraintViolation<StrengthRecordRequest>> violations =
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
            StrengthRecordRequest request,
            String propertyName) {

        Set<ConstraintViolation<StrengthRecordRequest>> violations =
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