package com.qianyi.sportstrainingassistant.model;

import java.time.DayOfWeek;
import java.util.List;

public class TrainingDayPlan {

    private DayOfWeek dayOfWeek;
    private String workoutName;
    private boolean restDay;
    private List<ExercisePlan> exercises;

    public TrainingDayPlan(
            DayOfWeek dayOfWeek,
            String workoutName,
            boolean restDay,
            List<ExercisePlan> exercises) {

        if (dayOfWeek == null) {
            throw new IllegalArgumentException(
                    "Day of week cannot be null"
            );
        }

        if (exercises == null) {
            throw new IllegalArgumentException(
                    "Exercises cannot be null"
            );
        }

        for (ExercisePlan exercise : exercises) {
            if (exercise == null) {
                throw new IllegalArgumentException(
                        "Exercises cannot contain null"
                );
            }
        }

        if (restDay) {

            if (!exercises.isEmpty()) {
                throw new IllegalArgumentException(
                        "Rest day cannot contain exercises"
                );
            }

        } else {

            if (workoutName == null || workoutName.isBlank()) {
                throw new IllegalArgumentException(
                        "Workout name cannot be null or blank for a training day"
                );
            }

            if (exercises.isEmpty()) {
                throw new IllegalArgumentException(
                        "Training day must contain at least one exercise"
                );
            }
        }

        this.dayOfWeek = dayOfWeek;
        this.workoutName = workoutName;
        this.restDay = restDay;
        this.exercises = List.copyOf(exercises);
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public boolean isRestDay() {
        return restDay;
    }

    public List<ExercisePlan> getExercises() {
        return exercises;
    }
}
