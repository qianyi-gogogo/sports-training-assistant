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

public class BasicBodyInfoRequestTest {

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

        BasicBodyInfoRequest request = createValidRequest();

        Set<ConstraintViolation<BasicBodyInfoRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Height ====================

    @Test
    void shouldRejectNullHeight() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setHeightCm(null);

        assertHasViolationFor(request, "heightCm");
    }


    @Test
    void shouldAcceptHeightBoundaryValues() {

        BasicBodyInfoRequest minRequest = createValidRequest();
        minRequest.setHeightCm(50.0);

        BasicBodyInfoRequest maxRequest = createValidRequest();
        maxRequest.setHeightCm(250.0);

        assertNoViolationFor(minRequest, "heightCm");
        assertNoViolationFor(maxRequest, "heightCm");
    }


    @Test
    void shouldRejectHeightBelowMinimum() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setHeightCm(49.9);

        assertHasViolationFor(request, "heightCm");
    }


    @Test
    void shouldRejectHeightAboveMaximum() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setHeightCm(250.1);

        assertHasViolationFor(request, "heightCm");
    }


    // ==================== Weight ====================

    @Test
    void shouldRejectNullWeight() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setWeightKg(null);

        assertHasViolationFor(request, "weightKg");
    }


    @Test
    void shouldAcceptWeightBoundaryValues() {

        BasicBodyInfoRequest minRequest = createValidRequest();
        minRequest.setWeightKg(10.0);

        BasicBodyInfoRequest maxRequest = createValidRequest();
        maxRequest.setWeightKg(300.0);

        assertNoViolationFor(minRequest, "weightKg");
        assertNoViolationFor(maxRequest, "weightKg");
    }


    @Test
    void shouldRejectWeightBelowMinimum() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setWeightKg(9.9);

        assertHasViolationFor(request, "weightKg");
    }


    @Test
    void shouldRejectWeightAboveMaximum() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setWeightKg(300.1);

        assertHasViolationFor(request, "weightKg");
    }


    // ==================== Body Fat Percentage ====================

    @Test
    void shouldAcceptNullBodyFatPercentage() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setBodyFatPercentage(null);

        assertNoViolationFor(request, "bodyFatPercentage");
    }


    @Test
    void shouldAcceptBodyFatBoundaryValues() {

        BasicBodyInfoRequest minRequest = createValidRequest();
        minRequest.setBodyFatPercentage(3.0);

        BasicBodyInfoRequest maxRequest = createValidRequest();
        maxRequest.setBodyFatPercentage(60.0);

        assertNoViolationFor(minRequest, "bodyFatPercentage");
        assertNoViolationFor(maxRequest, "bodyFatPercentage");
    }


    @Test
    void shouldRejectBodyFatBelowMinimum() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setBodyFatPercentage(2.9);

        assertHasViolationFor(request, "bodyFatPercentage");
    }


    @Test
    void shouldRejectBodyFatAboveMaximum() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setBodyFatPercentage(60.1);

        assertHasViolationFor(request, "bodyFatPercentage");
    }


    // ==================== Age ====================

    @Test
    void shouldRejectNullAge() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setAge(null);

        assertHasViolationFor(request, "age");
    }


    @Test
    void shouldAcceptAgeBoundaryValues() {

        BasicBodyInfoRequest minRequest = createValidRequest();
        minRequest.setAge(8);

        BasicBodyInfoRequest maxRequest = createValidRequest();
        maxRequest.setAge(100);

        assertNoViolationFor(minRequest, "age");
        assertNoViolationFor(maxRequest, "age");
    }


    @Test
    void shouldRejectAgeBelowMinimum() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setAge(7);

        assertHasViolationFor(request, "age");
    }


    @Test
    void shouldRejectAgeAboveMaximum() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setAge(101);

        assertHasViolationFor(request, "age");
    }


    // ==================== Biological Sex ====================

    @Test
    void shouldRejectNullBiologicalSex() {

        BasicBodyInfoRequest request = createValidRequest();
        request.setBiologicalSex(null);

        assertHasViolationFor(request, "biologicalSex");
    }


    // ==================== Helpers ====================

    private BasicBodyInfoRequest createValidRequest() {

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


    private void assertHasViolationFor(
            BasicBodyInfoRequest request,
            String propertyName) {

        Set<ConstraintViolation<BasicBodyInfoRequest>> violations =
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
            BasicBodyInfoRequest request,
            String propertyName) {

        Set<ConstraintViolation<BasicBodyInfoRequest>> violations =
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