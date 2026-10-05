package com.qianyi.sportstrainingassistant.model;

import java.util.List;

public class CompletedExercise {

    private final String exerciseName;
    private final List<CompletedSet> completedSets;

    public CompletedExercise(
            String exerciseName,
            List<CompletedSet> completedSets) {

        if (exerciseName == null || exerciseName.isBlank()) {
            throw new IllegalArgumentException(
                    "Exercise name cannot be null or blank"
            );
        }

        if (completedSets == null) {
            throw new IllegalArgumentException(
                    "Completed sets cannot be null"
            );
        }

        if (completedSets.isEmpty()) {
            throw new IllegalArgumentException(
                    "Completed sets cannot be empty"
            );
        }

        for (CompletedSet completedSet : completedSets) {
            if (completedSet == null) {
                throw new IllegalArgumentException(
                        "Completed sets cannot contain null"
                );
            }
        }

        this.exerciseName = exerciseName.strip();
        this.completedSets = List.copyOf(completedSets);
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public List<CompletedSet> getCompletedSets() {
        return completedSets;
    }
}