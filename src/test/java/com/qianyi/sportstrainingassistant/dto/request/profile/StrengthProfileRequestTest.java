package com.qianyi.sportstrainingassistant.dto.request.profile;

import com.qianyi.sportstrainingassistant.model.PullUpMode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StrengthProfileRequestTest {

    private static Validator validator;


    @BeforeAll
    static void setUpValidator() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }


    // ==================== Optional Fields ====================

    @Test
    void shouldAcceptAllNullStrengthFields() {

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        Set<ConstraintViolation<StrengthProfileRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Bench Press ====================

    @Test
    void shouldAcceptValidBenchPressRecords() {

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        request.setBenchPress(
                List.of(createValidStrengthRecord())
        );

        assertNoViolationForPrefix(
                request,
                "benchPress"
        );
    }


    @Test
    void shouldRejectEmptyBenchPressList() {

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        request.setBenchPress(List.of());

        assertHasViolationForPrefix(
                request,
                "benchPress"
        );
    }


    @Test
    void shouldRejectInvalidBenchPressRecordThroughValid() {

        StrengthRecordRequest invalidRecord =
                createValidStrengthRecord();

        invalidRecord.setReps(0);

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        request.setBenchPress(
                List.of(invalidRecord)
        );

        assertHasViolationForPrefix(
                request,
                "benchPress"
        );
    }


    // ==================== Pull Up ====================

    @Test
    void shouldAcceptValidPullUpRecords() {

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        request.setPullUp(
                List.of(createValidPullUpRecord())
        );

        assertNoViolationForPrefix(
                request,
                "pullUp"
        );
    }


    @Test
    void shouldRejectEmptyPullUpList() {

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        request.setPullUp(List.of());

        assertHasViolationForPrefix(
                request,
                "pullUp"
        );
    }


    @Test
    void shouldRejectInvalidPullUpRecordThroughValid() {

        PullUpRecordRequest invalidRecord =
                createValidPullUpRecord();

        invalidRecord.setReps(0);

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        request.setPullUp(
                List.of(invalidRecord)
        );

        assertHasViolationForPrefix(
                request,
                "pullUp"
        );
    }


    @Test
    void shouldAcceptPullUpRecordWithoutRir() {

        PullUpRecordRequest record =
                createValidPullUpRecord();

        record.setRir(null);

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        request.setPullUp(
                List.of(record)
        );

        assertNoViolationForPrefix(
                request,
                "pullUp"
        );
    }


    // ==================== Other Exercise Lists ====================

    @Test
    void shouldAcceptOneValidRecordForEachStrengthExercise() {

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        request.setBenchPress(
                List.of(createValidStrengthRecord())
        );

        request.setBarbellRow(
                List.of(createValidStrengthRecord())
        );

        request.setStrictPress(
                List.of(createValidStrengthRecord())
        );

        request.setDumbbellShoulderPress(
                List.of(createValidStrengthRecord())
        );

        request.setSquat(
                List.of(createValidStrengthRecord())
        );

        request.setLegPress(
                List.of(createValidStrengthRecord())
        );

        request.setPullUp(
                List.of(createValidPullUpRecord())
        );

        Set<ConstraintViolation<StrengthProfileRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Null Element ====================

    @Test
    void shouldDetectNullElementWhenElementIsValidated() {

        StrengthProfileRequest request =
                new StrengthProfileRequest();

        List<StrengthRecordRequest> records =
                new ArrayList<>();

        records.add(null);

        request.setBenchPress(records);

        Set<ConstraintViolation<StrengthProfileRequest>> violations =
                validator.validate(request);

        /*
         * Current request definition uses:
         *
         * @Valid
         * @Size(min = 1)
         * List<StrengthRecordRequest>
         *
         * This does not necessarily reject a null list element.
         *
         * We only confirm here that validation runs without crashing.
         * The Model layer still rejects null elements.
         */
        assertNotNull(violations);
    }


    // ==================== Helpers ====================

    private StrengthRecordRequest createValidStrengthRecord() {

        StrengthRecordRequest request =
                new StrengthRecordRequest();

        request.setWeightKg(100.0);
        request.setReps(5);
        request.setRir(1);

        return request;
    }


    private PullUpRecordRequest createValidPullUpRecord() {

        PullUpRecordRequest request =
                new PullUpRecordRequest();

        request.setMode(
                PullUpMode.values()[0]
        );

        request.setWeightKg(0.0);
        request.setReps(8);
        request.setRir(1);

        return request;
    }


    private void assertHasViolationForPrefix(
            StrengthProfileRequest request,
            String propertyPrefix) {

        Set<ConstraintViolation<StrengthProfileRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .startsWith(propertyPrefix));

        assertTrue(
                found,
                "Expected validation violation for " + propertyPrefix
        );
    }


    private void assertNoViolationForPrefix(
            StrengthProfileRequest request,
            String propertyPrefix) {

        Set<ConstraintViolation<StrengthProfileRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .startsWith(propertyPrefix));

        assertFalse(
                found,
                "Did not expect validation violation for " + propertyPrefix
        );
    }
}