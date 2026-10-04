package com.qianyi.sportstrainingassistant.model;

import java.time.LocalDate;
import java.util.List;

public class WeeklyTrainingPlan {

    private final int weekNumber;
    private final LocalDate startDate;
    private final List<TrainingDayPlan> days;


    public WeeklyTrainingPlan(
            int weekNumber,
            LocalDate startDate,
            List<TrainingDayPlan> days) {

        if (weekNumber < 1) {
            throw new IllegalArgumentException(
                    "Week number must be at least 1"
            );
        }

        if (startDate == null) {
            throw new IllegalArgumentException(
                    "Start date cannot be null"
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

        this.weekNumber = weekNumber;
        this.startDate = startDate;
        this.days = List.copyOf(days);
    }


    public int getWeekNumber() {
        return weekNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public List<TrainingDayPlan> getDays() {
        return days;
    }
}