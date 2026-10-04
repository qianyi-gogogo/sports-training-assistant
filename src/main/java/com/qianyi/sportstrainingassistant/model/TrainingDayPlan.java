package com.qianyi.sportstrainingassistant.model;

import java.util.List;

public class TrainingDayPlan {

    private final String workoutName;
    private final DayType dayType;
    private final List<ExercisePlan> exercises;


    public TrainingDayPlan(
            String workoutName,
            DayType dayType,
            List<ExercisePlan> exercises) {

        if (dayType == null) {
            throw new IllegalArgumentException(
                    "Day type cannot be null"
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

        if (dayType == DayType.REST && !exercises.isEmpty()) {
            throw new IllegalArgumentException(
                    "Rest day cannot contain exercises"
            );
        }

        if (dayType == DayType.TRAINING && exercises.isEmpty()) {
            throw new IllegalArgumentException(
                    "Training day must contain at least one exercise"
            );
        }

        if (workoutName == null || workoutName.isBlank()) {
            this.workoutName = null;
        } else {
            this.workoutName = workoutName.strip();
        }

        this.dayType = dayType;
        this.exercises = List.copyOf(exercises);
    }


    public String getWorkoutName() {
        return workoutName;
    }

    public DayType getDayType() {
        return dayType;
    }

    public List<ExercisePlan> getExercises() {
        return exercises;
    }
}