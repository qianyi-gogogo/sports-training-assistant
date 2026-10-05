package com.qianyi.sportstrainingassistant.dto.request.profile;

import com.qianyi.sportstrainingassistant.model.PullUpMode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PullUpRecordRequestTest {

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

        PullUpRecordRequest request =
                createValidRequest();

        Set<ConstraintViolation<PullUpRecordRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Mode ====================

    @Test
    void shouldRejectNullMode() {

        PullUpRecordRequest request =
                createValidRequest();

        request.setMode(null);

        assertHasViolationFor(
                request,
                "mode"
        );
    }


    // ==================== Weight ====================

    @Test
    void shouldRejectNullWeight() {

        PullUpRecordRequest request =
                createValidRequest();

        request.setWeightKg(null);

        assertHasViolationFor(
                request,
                "weightKg"
        );
    }


    @Test
    void shouldAcceptWeightBoundaryValues() {

        PullUpRecordRequest minRequest =
                createValidRequest();

        minRequest.setWeightKg(0.0);

        PullUpRecordRequest maxRequest =
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

        PullUpRecordRequest request =
                createValidRequest();

        request.setWeightKg(-0.1);

        assertHasViolationFor(
                request,
                "weightKg"
        );
    }


    @Test
    void shouldRejectWeightAboveMaximum() {

        PullUpRecordRequest request =
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

        PullUpRecordRequest request =
                createValidRequest();

        request.setReps(null);

        assertHasViolationFor(
                request,
                "reps"
        );
    }


    @Test
    void shouldAcceptPositiveReps() {

        PullUpRecordRequest request =
                createValidRequest();

        request.setReps(1);

        assertNoViolationFor(
                request,
                "reps"
        );
    }


    @Test
    void shouldRejectZeroReps() {

        PullUpRecordRequest request =
                createValidRequest();

        request.setReps(0);

        assertHasViolationFor(
                request,
                "reps"
        );
    }


    @Test
    void shouldRejectNegativeReps() {

        PullUpRecordRequest request =
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

        PullUpRecordRequest request =
                createValidRequest();

        request.setRir(null);

        assertNoViolationFor(
                request,
                "rir"
        );
    }


    @Test
    void shouldAcceptRirBoundaryValues() {

        PullUpRecordRequest zeroRequest =
                createValidRequest();

        zeroRequest.setRir(0);

        PullUpRecordRequest fiveRequest =
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

        PullUpRecordRequest request =
                createValidRequest();

        request.setRir(-1);

        assertHasViolationFor(
                request,
                "rir"
        );
    }


    @Test
    void shouldRejectRirAboveFive() {

        PullUpRecordRequest request =
                createValidRequest();

        request.setRir(6);

        assertHasViolationFor(
                request,
                "rir"
        );
    }


    // ==================== Helpers ====================

    private PullUpRecordRequest createValidRequest() {

        PullUpRecordRequest request =
                new PullUpRecordRequest();

        request.setMode(
                PullUpMode.values()[0]
        );

        request.setWeightKg(0.0);
        request.setReps(10);
        request.setRir(1);

        return request;
    }


    private void assertHasViolationFor(
            PullUpRecordRequest request,
            String propertyName) {

        Set<ConstraintViolation<PullUpRecordRequest>> violations =
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
            PullUpRecordRequest request,
            String propertyName) {

        Set<ConstraintViolation<PullUpRecordRequest>> violations =
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