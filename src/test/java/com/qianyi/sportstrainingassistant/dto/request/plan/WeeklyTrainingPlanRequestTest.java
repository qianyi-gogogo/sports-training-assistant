package com.qianyi.sportstrainingassistant.dto.request.plan;

import com.qianyi.sportstrainingassistant.model.DayType;
import com.qianyi.sportstrainingassistant.model.SetType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class WeeklyTrainingPlanRequestTest {

    private static Validator validator;


    @BeforeAll
    static void setUpValidator() {

        ValidatorFactory factory =
                Validation.buildDefaultValidatorFactory();

        validator = factory.getValidator();
    }


    // ==================== Valid Request ====================

    @Test
    void shouldAcceptValidWeeklyTrainingPlanRequest() {

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        Set<ConstraintViolation<WeeklyTrainingPlanRequest>> violations =
                validator.validate(request);

        assertTrue(violations.isEmpty());
    }


    // ==================== Week Number ====================

    @Test
    void shouldRejectNullWeekNumber() {

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        request.setWeekNumber(null);

        assertHasViolationForPrefix(
                request,
                "weekNumber"
        );
    }


    @Test
    void shouldRejectWeekNumberBelowOne() {

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        request.setWeekNumber(0);

        assertHasViolationForPrefix(
                request,
                "weekNumber"
        );
    }


    @Test
    void shouldAcceptWeekNumberOne() {

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        request.setWeekNumber(1);

        assertNoViolationForPrefix(
                request,
                "weekNumber"
        );
    }


    // ==================== Start Date ====================

    @Test
    void shouldRejectNullStartDate() {

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        request.setStartDate(null);

        assertHasViolationForPrefix(
                request,
                "startDate"
        );
    }


    // ==================== Days ====================

    @Test
    void shouldRejectNullDays() {

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        request.setDays(null);

        assertHasViolationForPrefix(
                request,
                "days"
        );
    }


    @Test
    void shouldRejectFewerThanSevenDays() {

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        request.setDays(
                List.of(
                        createValidTrainingDay(),
                        createValidTrainingDay(),
                        createValidTrainingDay(),
                        createValidTrainingDay(),
                        createValidTrainingDay(),
                        createValidTrainingDay()
                )
        );

        assertHasViolationForPrefix(
                request,
                "days"
        );
    }


    @Test
    void shouldRejectMoreThanSevenDays() {

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        List<TrainingDayPlanRequest> days =
                new ArrayList<>(
                        createSevenValidDays()
                );

        days.add(
                createValidTrainingDay()
        );

        request.setDays(days);

        assertHasViolationForPrefix(
                request,
                "days"
        );
    }


    @Test
    void shouldAcceptExactlySevenDays() {

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        request.setDays(
                createSevenValidDays()
        );

        assertNoViolationForPrefix(
                request,
                "days"
        );
    }


    // ==================== Nested Validation ====================

    @Test
    void shouldRejectInvalidNestedTrainingDayThroughValid() {

        List<TrainingDayPlanRequest> days =
                new ArrayList<>(
                        createSevenValidDays()
                );

        TrainingDayPlanRequest invalidDay =
                createValidTrainingDay();

        invalidDay.setDayType(null);

        days.set(0, invalidDay);

        WeeklyTrainingPlanRequest request =
                createValidWeeklyRequest();

        request.setDays(days);

        assertHasViolationForPrefix(
                request,
                "days"
        );
    }


    // ==================== Helpers ====================

    private WeeklyTrainingPlanRequest createValidWeeklyRequest() {

        WeeklyTrainingPlanRequest request =
                new WeeklyTrainingPlanRequest();

        request.setWeekNumber(1);
        request.setStartDate(
                LocalDate.of(2026, 10, 5)
        );
        request.setDays(
                createSevenValidDays()
        );

        return request;
    }


    private List<TrainingDayPlanRequest> createSevenValidDays() {

        List<TrainingDayPlanRequest> days =
                new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            days.add(
                    createValidTrainingDay()
            );
        }

        return days;
    }


    private TrainingDayPlanRequest createValidTrainingDay() {

        TrainingDayPlanRequest request =
                new TrainingDayPlanRequest();

        request.setWorkoutName(
                "Training Day"
        );

        request.setDayType(
                DayType.TRAINING
        );

        request.setExercises(
                List.of(
                        createValidExercisePlan()
                )
        );

        return request;
    }


    private ExercisePlanRequest createValidExercisePlan() {

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
            WeeklyTrainingPlanRequest request,
            String propertyPrefix) {

        Set<ConstraintViolation<WeeklyTrainingPlanRequest>> violations =
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
            WeeklyTrainingPlanRequest request,
            String propertyPrefix) {

        Set<ConstraintViolation<WeeklyTrainingPlanRequest>> violations =
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