package com.qianyi.sportstrainingassistant.model;

import java.util.List;

public class ExercisePlan {

    private String exerciseName;
    private List<PlannedSet> plannedSets;

    public ExercisePlan(
            String exerciseName,
            List<PlannedSet> plannedSets) {

        if (exerciseName == null || exerciseName.isBlank()) {
            throw new IllegalArgumentException(
                    "Exercise name cannot be null or blank"
            );
        }

        if (plannedSets == null || plannedSets.isEmpty()) {
            throw new IllegalArgumentException(
                    "Planned sets must contain at least one set"
            );
        }

        for (PlannedSet set : plannedSets) {
            if (set == null) {
                throw new IllegalArgumentException(
                        "Planned sets cannot contain null"
                );
            }
        }

        this.exerciseName = exerciseName;
        this.plannedSets = List.copyOf(plannedSets);
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public List<PlannedSet> getPlannedSets() {
        return plannedSets;
    }
}
