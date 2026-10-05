package com.qianyi.sportstrainingassistant.dto.request.goal;

import com.qianyi.sportstrainingassistant.model.TrainingGoalType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TrainingGoalRequestTest {

    private static Validator validator;


    @BeforeAll
    static void setUpValidator() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }


    // ==================== Valid Request ====================

    @Test
    void shouldAcceptValidGoalPriorities() {

        TrainingGoalRequest request =
                createValidRequest();

        Set<ConstraintViolation<TrainingGoalRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Goal Priorities ====================

    @Test
    void shouldRejectNullGoalPriorities() {

        TrainingGoalRequest request =
                createValidRequest();

        request.setGoalPriorities(null);

        assertHasViolationFor(
                request,
                "goalPriorities"
        );
    }


    @Test
    void shouldRejectTooFewGoalPriorities() {

        TrainingGoalRequest request =
                createValidRequest();

        request.setGoalPriorities(
                Arrays.asList(
                        TrainingGoalType.values()[0],
                        TrainingGoalType.values()[1],
                        TrainingGoalType.values()[2],
                        TrainingGoalType.values()[3]
                )
        );

        assertHasViolationFor(
                request,
                "goalPriorities"
        );
    }


    @Test
    void shouldRejectTooManyGoalPriorities() {

        TrainingGoalRequest request =
                createValidRequest();

        List<TrainingGoalType> goals =
                new ArrayList<>(
                        Arrays.asList(
                                TrainingGoalType.values()
                        )
                );

        goals.add(
                TrainingGoalType.values()[0]
        );

        request.setGoalPriorities(goals);

        assertHasViolationFor(
                request,
                "goalPriorities"
        );
    }


    @Test
    void shouldRejectNullGoalInsidePriorityList() {

        TrainingGoalRequest request =
                createValidRequest();

        List<TrainingGoalType> goals =
                new ArrayList<>(
                        Arrays.asList(
                                TrainingGoalType.values()
                        )
                );

        goals.set(0, null);

        request.setGoalPriorities(goals);

        Set<ConstraintViolation<TrainingGoalRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .startsWith("goalPriorities"));

        assertTrue(found);
    }


    // ==================== Specific Goal Details ====================

    @Test
    void shouldAcceptNullSpecificGoalDetails() {

        TrainingGoalRequest request =
                createValidRequest();

        request.setSpecificGoalDetails(null);

        Set<ConstraintViolation<TrainingGoalRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    @Test
    void shouldAcceptBlankSpecificGoalDetails() {

        TrainingGoalRequest request =
                createValidRequest();

        request.setSpecificGoalDetails("   ");

        Set<ConstraintViolation<TrainingGoalRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    @Test
    void shouldAcceptSpecificGoalDetailsText() {

        TrainingGoalRequest request =
                createValidRequest();

        request.setSpecificGoalDetails(
                "Improve upper body strength"
        );

        Set<ConstraintViolation<TrainingGoalRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Helpers ====================

    private TrainingGoalRequest createValidRequest() {

        TrainingGoalRequest request =
                new TrainingGoalRequest();

        request.setGoalPriorities(
                Arrays.asList(
                        TrainingGoalType.values()
                )
        );

        request.setSpecificGoalDetails(
                "Build muscle and improve strength"
        );

        return request;
    }


    private void assertHasViolationFor(
            TrainingGoalRequest request,
            String propertyName) {

        Set<ConstraintViolation<TrainingGoalRequest>> violations =
                validator.validate(request);

        boolean found =
                violations.stream()
                        .anyMatch(violation ->
                                violation.getPropertyPath()
                                        .toString()
                                        .startsWith(propertyName));

        assertTrue(
                found,
                "Expected validation violation for " + propertyName
        );
    }
}