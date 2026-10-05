package com.qianyi.sportstrainingassistant.dto.request.plan;

import com.qianyi.sportstrainingassistant.model.SetType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExercisePlanRequestTest {

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

        ExercisePlanRequest request =
                createValidRequest();

        Set<ConstraintViolation<ExercisePlanRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    // ==================== Exercise Name ====================

    @Test
    void shouldRejectNullExerciseName() {

        ExercisePlanRequest request =
                createValidRequest();

        request.setExerciseName(null);

        assertHasViolationForPrefix(
                request,
                "exerciseName"
        );
    }

    @Test
    void shouldRejectEmptyExerciseName() {

        ExercisePlanRequest request =
                createValidRequest();

        request.setExerciseName("");

        assertHasViolationForPrefix(
                request,
                "exerciseName"
        );
    }

    @Test
    void shouldRejectBlankExerciseName() {

        ExercisePlanRequest request =
                createValidRequest();

        request.setExerciseName("   ");

        assertHasViolationForPrefix(
                request,
                "exerciseName"
        );
    }

    // ==================== Sets ====================

    @Test
    void shouldRejectNullSets() {

        ExercisePlanRequest request =
                createValidRequest();

        request.setSets(null);

        assertHasViolationForPrefix(
                request,
                "sets"
        );
    }

    @Test
    void shouldRejectEmptySets() {

        ExercisePlanRequest request =
                createValidRequest();

        request.setSets(List.of());

        assertHasViolationForPrefix(
                request,
                "sets"
        );
    }

    @Test
    void shouldAcceptOneValidSet() {

        ExercisePlanRequest request =
                new ExercisePlanRequest();

        request.setExerciseName(
                "Bench Press"
        );

        request.setSets(
                List.of(
                        createValidPlannedSet()
                )
        );

        Set<ConstraintViolation<ExercisePlanRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    // ==================== Nested Validation ====================

    @Test
    void shouldRejectInvalidNestedPlannedSetThroughValid() {

        PlannedSetRequest invalidSet =
                createValidPlannedSet();

        invalidSet.setMinReps(0);

        ExercisePlanRequest request =
                new ExercisePlanRequest();

        request.setExerciseName(
                "Bench Press"
        );

        request.setSets(
                List.of(invalidSet)
        );

        assertHasViolationForPrefix(
                request,
                "sets"
        );
    }

    // ==================== Helpers ====================

    private ExercisePlanRequest createValidRequest() {

        ExercisePlanRequest request =
                new ExercisePlanRequest();

        request.setExerciseName(
                "Bench Press"
        );

        request.setSets(
                List.of(
                        createValidPlannedSet()
                )
        );

        return request;
    }

    private PlannedSetRequest createValidPlannedSet() {

        PlannedSetRequest request =
                new PlannedSetRequest();

        request.setSetType(
                SetType.values()[0]
        );

        request.setWeightKg(100.0);
        request.setMinReps(5);
        request.setMaxReps(8);

        return request;
    }

    private void assertHasViolationForPrefix(
            ExercisePlanRequest request,
            String propertyPrefix) {

        Set<ConstraintViolation<ExercisePlanRequest>> violations =
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
}