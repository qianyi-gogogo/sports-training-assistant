package com.qianyi.sportstrainingassistant.dto.request.plan;

import com.qianyi.sportstrainingassistant.model.DayType;
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

public class TrainingDayPlanRequestTest {

    private static Validator validator;


    @BeforeAll
    static void setUpValidator() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }


    // ==================== Valid Request ====================

    @Test
    void shouldAcceptValidTrainingDayRequest() {

        TrainingDayPlanRequest request =
                createValidTrainingDayRequest();

        Set<ConstraintViolation<TrainingDayPlanRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Workout Name ====================

    @Test
    void shouldAcceptNullWorkoutName() {

        TrainingDayPlanRequest request =
                createValidTrainingDayRequest();

        request.setWorkoutName(null);

        Set<ConstraintViolation<TrainingDayPlanRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    @Test
    void shouldAcceptBlankWorkoutName() {

        TrainingDayPlanRequest request =
                createValidTrainingDayRequest();

        request.setWorkoutName("   ");

        Set<ConstraintViolation<TrainingDayPlanRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Day Type ====================

    @Test
    void shouldRejectNullDayType() {

        TrainingDayPlanRequest request =
                createValidTrainingDayRequest();

        request.setDayType(null);

        assertHasViolationForPrefix(
                request,
                "dayType"
        );
    }


    // ==================== Exercises ====================

    @Test
    void shouldRejectNullExercises() {

        TrainingDayPlanRequest request =
                createValidTrainingDayRequest();

        request.setExercises(null);

        assertHasViolationForPrefix(
                request,
                "exercises"
        );
    }


    @Test
    void shouldAcceptEmptyExercisesAtRequestLevel() {

        TrainingDayPlanRequest request =
                createValidTrainingDayRequest();

        request.setExercises(List.of());

        Set<ConstraintViolation<TrainingDayPlanRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Rest Day ====================

    @Test
    void shouldAcceptRestDayWithEmptyExercises() {

        TrainingDayPlanRequest request =
                new TrainingDayPlanRequest();

        request.setWorkoutName("Recovery Day");
        request.setDayType(DayType.REST);
        request.setExercises(List.of());

        Set<ConstraintViolation<TrainingDayPlanRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    @Test
    void shouldAcceptRestDayWithExercises() {

        TrainingDayPlanRequest request =
                new TrainingDayPlanRequest();

        request.setWorkoutName("Active Recovery");
        request.setDayType(DayType.REST);
        request.setExercises(
                List.of(createValidExercisePlan())
        );

        Set<ConstraintViolation<TrainingDayPlanRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Nested Validation ====================

    @Test
    void shouldRejectInvalidNestedExerciseThroughValid() {

        ExercisePlanRequest invalidExercise =
                createValidExercisePlan();

        invalidExercise.setExerciseName("   ");

        TrainingDayPlanRequest request =
                new TrainingDayPlanRequest();

        request.setWorkoutName("Chest Day");
        request.setDayType(DayType.TRAINING);
        request.setExercises(
                List.of(invalidExercise)
        );

        assertHasViolationForPrefix(
                request,
                "exercises"
        );
    }


    // ==================== Helpers ====================

    private TrainingDayPlanRequest createValidTrainingDayRequest() {

        TrainingDayPlanRequest request =
                new TrainingDayPlanRequest();

        request.setWorkoutName("Chest Day");
        request.setDayType(DayType.TRAINING);
        request.setExercises(
                List.of(createValidExercisePlan())
        );

        return request;
    }


    private ExercisePlanRequest createValidExercisePlan() {

        ExercisePlanRequest request =
                new ExercisePlanRequest();

        request.setExerciseName("Bench Press");

        request.setSets(
                List.of(createValidPlannedSet())
        );

        return request;
    }


    private PlannedSetRequest createValidPlannedSet() {

        PlannedSetRequest request =
                new PlannedSetRequest();

        request.setSetType(SetType.values()[0]);
        request.setWeightKg(100.0);
        request.setMinReps(5);
        request.setMaxReps(8);

        return request;
    }


    private void assertHasViolationForPrefix(
            TrainingDayPlanRequest request,
            String propertyPrefix) {

        Set<ConstraintViolation<TrainingDayPlanRequest>> violations =
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