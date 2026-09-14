package com.qianyi.sportstrainingassistant.model;

import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WeeklyTrainingPlanTest {

    private TrainingDayPlan createTrainingDay(
            DayOfWeek dayOfWeek,
            String workoutName) {

        PlannedSet set = new PlannedSet(
                SetType.WORKING,
                70.0,
                4,
                6
        );

        ExercisePlan exercise = new ExercisePlan(
                "Bench Press",
                List.of(set)
        );

        return new TrainingDayPlan(
                dayOfWeek,
                workoutName,
                false,
                List.of(exercise)
        );
    }

    private TrainingDayPlan createRestDay(
            DayOfWeek dayOfWeek) {

        return new TrainingDayPlan(
                dayOfWeek,
                null,
                true,
                List.of()
        );
    }


    @Test
    void shouldCreateWeeklyTrainingPlan() {

        List<TrainingDayPlan> days = List.of(
                createTrainingDay(
                        DayOfWeek.MONDAY,
                        "Chest"
                ),
                createTrainingDay(
                        DayOfWeek.TUESDAY,
                        "Back"
                ),
                createRestDay(
                        DayOfWeek.WEDNESDAY
                ),
                createTrainingDay(
                        DayOfWeek.THURSDAY,
                        "Shoulders"
                ),
                createTrainingDay(
                        DayOfWeek.FRIDAY,
                        "Legs"
                ),
                createTrainingDay(
                        DayOfWeek.SATURDAY,
                        "Arms"
                ),
                createRestDay(
                        DayOfWeek.SUNDAY
                )
        );

        WeeklyTrainingPlan plan = new WeeklyTrainingPlan(
                1,
                LocalDate.of(2026, 9, 14),
                days
        );

        assertEquals(
                1,
                plan.getWeekNumber()
        );

        assertEquals(
                LocalDate.of(2026, 9, 14),
                plan.getWeekStartDate()
        );

        assertEquals(
                days,
                plan.getDays()
        );
    }


    @Test
    void shouldPreserveTrainingDayOrder() {

        TrainingDayPlan monday = createTrainingDay(
                DayOfWeek.MONDAY,
                "Chest"
        );

        TrainingDayPlan tuesday = createTrainingDay(
                DayOfWeek.TUESDAY,
                "Back"
        );

        TrainingDayPlan wednesday = createRestDay(
                DayOfWeek.WEDNESDAY
        );

        List<TrainingDayPlan> days = List.of(
                monday,
                tuesday,
                wednesday,
                createTrainingDay(
                        DayOfWeek.THURSDAY,
                        "Shoulders"
                ),
                createTrainingDay(
                        DayOfWeek.FRIDAY,
                        "Legs"
                ),
                createTrainingDay(
                        DayOfWeek.SATURDAY,
                        "Arms"
                ),
                createRestDay(
                        DayOfWeek.SUNDAY
                )
        );

        WeeklyTrainingPlan plan = new WeeklyTrainingPlan(
                1,
                LocalDate.of(2026, 9, 14),
                days
        );

        assertEquals(
                monday,
                plan.getDays().get(0)
        );

        assertEquals(
                tuesday,
                plan.getDays().get(1)
        );

        assertEquals(
                wednesday,
                plan.getDays().get(2)
        );
    }
    // ==================== Validation ====================

    @Test
    void shouldRejectNullWeekNumber() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        null,
                        LocalDate.of(2026, 9, 14),
                        createValidWeek()
                )
        );
    }


    @Test
    void shouldRejectWeekNumberBelowOne() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        0,
                        LocalDate.of(2026, 9, 14),
                        createValidWeek()
                )
        );
    }


    @Test
    void shouldRejectNullWeekStartDate() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        null,
                        createValidWeek()
                )
        );
    }


    @Test
    void shouldRejectNullDays() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 9, 14),
                        null
                )
        );
    }


    @Test
    void shouldRejectIncompleteWeek() {

        List<TrainingDayPlan> days = List.of(
                createTrainingDay(DayOfWeek.MONDAY, "Chest"),
                createTrainingDay(DayOfWeek.TUESDAY, "Back")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 9, 14),
                        days
                )
        );
    }


    @Test
    void shouldRejectDuplicateDayOfWeek() {

        List<TrainingDayPlan> days = List.of(
                createTrainingDay(DayOfWeek.MONDAY, "Chest"),
                createTrainingDay(DayOfWeek.MONDAY, "Back"),
                createRestDay(DayOfWeek.WEDNESDAY),
                createTrainingDay(DayOfWeek.THURSDAY, "Shoulders"),
                createTrainingDay(DayOfWeek.FRIDAY, "Legs"),
                createTrainingDay(DayOfWeek.SATURDAY, "Arms"),
                createRestDay(DayOfWeek.SUNDAY)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 9, 14),
                        days
                )
        );
    }


    @Test
    void shouldRejectNullInsideDays() {

        List<TrainingDayPlan> days = new java.util.ArrayList<>(
                createValidWeek()
        );

        days.set(3, null);

        assertThrows(
                IllegalArgumentException.class,
                () -> new WeeklyTrainingPlan(
                        1,
                        LocalDate.of(2026, 9, 14),
                        days
                )
        );
    }


    @Test
    void shouldProtectDaysFromExternalModification() {

        List<TrainingDayPlan> days = new java.util.ArrayList<>(
                createValidWeek()
        );

        WeeklyTrainingPlan plan = new WeeklyTrainingPlan(
                1,
                LocalDate.of(2026, 9, 14),
                days
        );

        days.clear();

        assertEquals(
                7,
                plan.getDays().size()
        );
    }//helper
    private List<TrainingDayPlan> createValidWeek() {

        return List.of(
                createTrainingDay(
                        DayOfWeek.MONDAY,
                        "Chest"
                ),
                createTrainingDay(
                        DayOfWeek.TUESDAY,
                        "Back"
                ),
                createRestDay(
                        DayOfWeek.WEDNESDAY
                ),
                createTrainingDay(
                        DayOfWeek.THURSDAY,
                        "Shoulders"
                ),
                createTrainingDay(
                        DayOfWeek.FRIDAY,
                        "Legs"
                ),
                createTrainingDay(
                        DayOfWeek.SATURDAY,
                        "Arms"
                ),
                createRestDay(
                        DayOfWeek.SUNDAY
                )
        );
    }
}