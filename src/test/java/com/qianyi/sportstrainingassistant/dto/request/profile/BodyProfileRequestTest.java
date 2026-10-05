package com.qianyi.sportstrainingassistant.dto.request.profile;

import com.qianyi.sportstrainingassistant.model.BiologicalSex;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BodyProfileRequestTest {

    private static Validator validator;


    @BeforeAll
    static void setUpValidator() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }


    // ==================== Basic Body Info ====================

    @Test
    void shouldRejectNullBasicBodyInfo() {

        BodyProfileRequest request =
                new BodyProfileRequest();

        request.setBasicBodyInfo(null);

        Set<ConstraintViolation<BodyProfileRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .equals("basicBodyInfo"));

        assertTrue(found);
    }


    @Test
    void shouldAcceptValidBasicBodyInfo() {

        BodyProfileRequest request =
                new BodyProfileRequest();

        request.setBasicBodyInfo(
                createValidBasicBodyInfo()
        );

        Set<ConstraintViolation<BodyProfileRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    @Test
    void shouldRejectInvalidNestedBasicBodyInfoThroughValid() {

        BasicBodyInfoRequest basicBodyInfo =
                createValidBasicBodyInfo();

        basicBodyInfo.setAge(7);

        BodyProfileRequest request =
                new BodyProfileRequest();

        request.setBasicBodyInfo(
                basicBodyInfo
        );

        Set<ConstraintViolation<BodyProfileRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .startsWith("basicBodyInfo"));

        assertTrue(found);
    }


    // ==================== Optional Body Measurements ====================

    @Test
    void shouldAcceptNullBodyMeasurements() {

        BodyProfileRequest request =
                new BodyProfileRequest();

        request.setBasicBodyInfo(
                createValidBasicBodyInfo()
        );

        request.setBodyMeasurements(null);

        Set<ConstraintViolation<BodyProfileRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    @Test
    void shouldRejectInvalidNestedBodyMeasurementsThroughValid() {

        BodyMeasurementsRequest measurements =
                new BodyMeasurementsRequest();

        measurements.setArmCircumferenceCm(5.0);

        BodyProfileRequest request =
                new BodyProfileRequest();

        request.setBasicBodyInfo(
                createValidBasicBodyInfo()
        );

        request.setBodyMeasurements(
                measurements
        );

        Set<ConstraintViolation<BodyProfileRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .startsWith("bodyMeasurements"));

        assertTrue(found);
    }


    // ==================== Optional Strength Profile ====================

    @Test
    void shouldAcceptNullStrengthProfile() {

        BodyProfileRequest request =
                new BodyProfileRequest();

        request.setBasicBodyInfo(
                createValidBasicBodyInfo()
        );

        request.setStrengthProfile(null);

        Set<ConstraintViolation<BodyProfileRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    @Test
    void shouldRejectInvalidNestedStrengthProfileThroughValid() {

        StrengthRecordRequest invalidRecord =
                new StrengthRecordRequest();

        invalidRecord.setWeightKg(100.0);
        invalidRecord.setReps(0);
        invalidRecord.setRir(1);

        StrengthProfileRequest strengthProfile =
                new StrengthProfileRequest();

        strengthProfile.setBenchPress(
                java.util.List.of(invalidRecord)
        );

        BodyProfileRequest request =
                new BodyProfileRequest();

        request.setBasicBodyInfo(
                createValidBasicBodyInfo()
        );

        request.setStrengthProfile(
                strengthProfile
        );

        Set<ConstraintViolation<BodyProfileRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .startsWith("strengthProfile"));

        assertTrue(found);
    }


    // ==================== Fully Valid Request ====================

    @Test
    void shouldAcceptFullyValidBodyProfile() {

        BodyMeasurementsRequest measurements =
                new BodyMeasurementsRequest();

        measurements.setChestCircumferenceCm(100.0);
        measurements.setWaistCircumferenceCm(80.0);

        StrengthRecordRequest benchPressRecord =
                new StrengthRecordRequest();

        benchPressRecord.setWeightKg(80.0);
        benchPressRecord.setReps(5);
        benchPressRecord.setRir(1);

        StrengthProfileRequest strengthProfile =
                new StrengthProfileRequest();

        strengthProfile.setBenchPress(
                java.util.List.of(benchPressRecord)
        );

        BodyProfileRequest request =
                new BodyProfileRequest();

        request.setBasicBodyInfo(
                createValidBasicBodyInfo()
        );

        request.setBodyMeasurements(
                measurements
        );

        request.setStrengthProfile(
                strengthProfile
        );

        Set<ConstraintViolation<BodyProfileRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Helpers ====================

    private BasicBodyInfoRequest createValidBasicBodyInfo() {

        BasicBodyInfoRequest request =
                new BasicBodyInfoRequest();

        request.setHeightCm(175.0);
        request.setWeightKg(70.0);
        request.setBodyFatPercentage(15.0);
        request.setAge(20);

        request.setBiologicalSex(
                BiologicalSex.values()[0]
        );

        return request;
    }
}