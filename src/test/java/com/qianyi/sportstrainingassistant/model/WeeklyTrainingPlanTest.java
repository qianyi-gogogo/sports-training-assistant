package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeeklyTrainingPlanTest {


    private TrainingDayPlan createTrainingDay(String name) {

        PlannedSet set =
                new PlannedSet(
                        SetType.WORKING,
                        70.0,
                        6,
                        10
                );

        ExercisePlan exercise =
                new ExercisePlan(
                        "Bench Press",
                        List.of(set)
                );

        return new TrainingDayPlan(
                name,
                DayType.TRAINING,
                List.of(exercise)
        );
    }


    private TrainingDayPlan createRestDay() {

        return new TrainingDayPlan(
                "Rest Day",
                DayType.REST,
                List.of()
        );
    }


    private List<TrainingDayPlan> createSevenDays() {

        return List.of(
                createTrainingDay("Chest"),
                createTrainingDay("Back"),
                createRestDay(),
                createTrainingDay("Shoulders"),
                createTrainingDay("Legs"),
                createTrainingDay("Arms"),
                createRestDay()
        );
    }


    // ==================== Valid Weekly Plan ====================

    @Test
    void shouldCreateValidWeeklyTrainingPlan() {

        LocalDate startDate =
                LocalDate.of(
                        2026,
                        10,
                        5
                );

        WeeklyTrainingPlan plan =
                new WeeklyTrainingPlan(
                        1,
                        startDate,
                        createSevenDays()
                );

        assertEquals(
                1,
                plan.getWeekNumber()
        );

        assertEquals(
                startDate,
                plan.getStartDate()
        );

        assertEquals(
                7,
                plan.getDays().size()
        );
    }


    // ==================== Week Number ====================

    @Test
    void shouldAcceptWeekNumberOne() {

        WeeklyTrainingPlan plan =
                new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 10, 5),
                        createSevenDays()
                );

        assertEquals(
                1,
                plan.getWeekNumber()
        );
    }


    @Test
    void shouldAcceptHigherWeekNumber() {

        WeeklyTrainingPlan plan =
                new WeeklyTrainingPlan(
                        52,
                        LocalDate.of(2027, 9, 27),
                        createSevenDays()
                );

        assertEquals(
                52,
                plan.getWeekNumber()
        );
    }


    @Test
    void shouldRejectZeroWeekNumber() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        0,
                        LocalDate.of(2026, 10, 5),
                        createSevenDays()
                )
        );
    }


    @Test
    void shouldRejectNegativeWeekNumber() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        -1,
                        LocalDate.of(2026, 10, 5),
                        createSevenDays()
                )
        );
    }


    // ==================== Start Date ====================

    @Test
    void shouldRejectNullStartDate() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        null,
                        createSevenDays()
                )
        );
    }


    // ==================== Days ====================

    @Test
    void shouldRejectNullDays() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 10, 5),
                        null
                )
        );
    }


    @Test
    void shouldRejectLessThanSevenDays() {

        List<TrainingDayPlan> days =
                List.of(
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 10, 5),
                        days
                )
        );
    }


    @Test
    void shouldRejectMoreThanSevenDays() {

        List<TrainingDayPlan> days =
                List.of(
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay()
                );

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 10, 5),
                        days
                )
        );
    }


    @Test
    void shouldRejectNullDayInsideList() {

        List<TrainingDayPlan> days =
                new ArrayList<>();

        days.add(createRestDay());
        days.add(createRestDay());
        days.add(createRestDay());
        days.add(null);
        days.add(createRestDay());
        days.add(createRestDay());
        days.add(createRestDay());

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 10, 5),
                        days
                )
        );
    }


    // ==================== Rest Days ====================

    @Test
    void shouldAllowSevenRestDays() {

        List<TrainingDayPlan> days =
                List.of(
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay(),
                        createRestDay()
                );

        WeeklyTrainingPlan plan =
                new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 10, 5),
                        days
                );

        assertEquals(
                7,
                plan.getDays().size()
        );

        assertTrue(
                plan.getDays()
                        .stream()
                        .allMatch(
                                day ->
                                        day.getDayType()
                                                == DayType.REST
                        )
        );
    }


    // ==================== Order ====================

    @Test
    void shouldPreserveDayOrder() {

        List<TrainingDayPlan> days =
                List.of(
                        createTrainingDay("Day 1"),
                        createTrainingDay("Day 2"),
                        createRestDay(),
                        createTrainingDay("Day 4"),
                        createTrainingDay("Day 5"),
                        createTrainingDay("Day 6"),
                        createRestDay()
                );

        WeeklyTrainingPlan plan =
                new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 10, 5),
                        days
                );

        assertEquals(
                "Day 1",
                plan.getDays()
                        .get(0)
                        .getWorkoutName()
        );

        assertEquals(
                "Day 2",
                plan.getDays()
                        .get(1)
                        .getWorkoutName()
        );

        assertEquals(
                "Day 4",
                plan.getDays()
                        .get(3)
                        .getWorkoutName()
        );
    }


    // ==================== Defensive Copy ====================

    @Test
    void shouldProtectDaysFromExternalModification() {

        List<TrainingDayPlan> days =
                new ArrayList<>(
                        createSevenDays()
                );

        WeeklyTrainingPlan plan =
                new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 10, 5),
                        days
                );

        days.clear();

        assertEquals(
                7,
                plan.getDays().size()
        );
    }


    @Test
    void returnedDaysShouldNotBeModifiable() {

        WeeklyTrainingPlan plan =
                new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 10, 5),
                        createSevenDays()
                );

        assertThrows(
                UnsupportedOperationException.class,
                () -> plan.getDays().clear()
        );
    }
}