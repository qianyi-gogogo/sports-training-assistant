package com.qianyi.sportstrainingassistant.model;

import java.time.LocalDate;
import java.util.List;

public class CompletedWorkout {

    private final LocalDate date;
    private final List<CompletedExercise> completedExercises;
    private final DailyTrainingFeedback feedback;

    public CompletedWorkout(
            LocalDate date,
            List<CompletedExercise> completedExercises,
            DailyTrainingFeedback feedback) {

        if (date == null) {
            throw new IllegalArgumentException(
                    "Date cannot be null"
            );
        }

        if (completedExercises == null) {
            throw new IllegalArgumentException(
                    "Completed exercises cannot be null"
            );
        }

        for (CompletedExercise completedExercise : completedExercises) {
            if (completedExercise == null) {
                throw new IllegalArgumentException(
                        "Completed exercises cannot contain null"
                );
            }
        }

        this.date = date;
        this.completedExercises = List.copyOf(completedExercises);
        this.feedback = feedback;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<CompletedExercise> getCompletedExercises() {
        return completedExercises;
    }

    public DailyTrainingFeedback getFeedback() {
        return feedback;
    }
}