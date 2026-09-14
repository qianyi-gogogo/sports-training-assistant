package com.qianyi.sportstrainingassistant.model;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class WeeklyTrainingPlan {

    private Integer weekNumber;
    private LocalDate weekStartDate;
    private List<TrainingDayPlan> days;

    public WeeklyTrainingPlan(
            Integer weekNumber,
            LocalDate weekStartDate,
            List<TrainingDayPlan> days) {

        if (weekNumber == null || weekNumber < 1) {
            throw new IllegalArgumentException(
                    "Week number must be at least 1"
            );
        }

        if (weekStartDate == null) {
            throw new IllegalArgumentException(
                    "Week start date cannot be null"
            );
        }

        if (days == null) {
            throw new IllegalArgumentException(
                    "Training days cannot be null"
            );
        }

        if (days.size() != 7) {
            throw new IllegalArgumentException(
                    "Weekly training plan must contain exactly 7 days"
            );
        }

        for (TrainingDayPlan day : days) {
            if (day == null) {
                throw new IllegalArgumentException(
                        "Training days cannot contain null"
                );
            }
        }

        Set<DayOfWeek> uniqueDays = EnumSet.noneOf(DayOfWeek.class);

        for (TrainingDayPlan day : days) {
            if (!uniqueDays.add(day.getDayOfWeek())) {
                throw new IllegalArgumentException(
                        "Each day of the week can appear only once"
                );
            }
        }

        if (uniqueDays.size() != 7) {
            throw new IllegalArgumentException(
                    "Weekly training plan must contain every day of the week"
            );
        }

        this.weekNumber = weekNumber;
        this.weekStartDate = weekStartDate;
        this.days = List.copyOf(days);
    }

    public Integer getWeekNumber() {
        return weekNumber;
    }

    public LocalDate getWeekStartDate() {
        return weekStartDate;
    }

    public List<TrainingDayPlan> getDays() {
        return days;
    }
}

