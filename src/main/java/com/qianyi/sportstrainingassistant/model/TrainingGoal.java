package com.qianyi.sportstrainingassistant.model;

import java.util.List;

public class TrainingGoal {

    private List<TrainingGoalType> goalPriorities;
    private String specificGoalDetails;

    public TrainingGoal(
            List<TrainingGoalType> goalPriorities,
            String specificGoalDetails) {

        setGoalPriorities(goalPriorities);
        this.specificGoalDetails = specificGoalDetails;
    }

    public List<TrainingGoalType> getGoalPriorities() {
        return goalPriorities;
    }

    public void setGoalPriorities(List<TrainingGoalType> goalPriorities) {

        // The user must rank all five training goals.
        if (goalPriorities == null
                || goalPriorities.size() != TrainingGoalType.values().length) {

            throw new IllegalArgumentException(
                    "All training goals must be ranked"
            );
        }

        // No goal in the priority list can be null.
        for (TrainingGoalType goal : goalPriorities) {
            if (goal == null) {
                throw new IllegalArgumentException(
                        "Goal priorities cannot contain null"
                );
            }
        }

        // Each training goal can appear only once.
        if (goalPriorities.stream().distinct().count()
                != goalPriorities.size()) {

            throw new IllegalArgumentException(
                    "Goal priorities cannot contain duplicate goals"
            );
        }

        this.goalPriorities = List.copyOf(goalPriorities);
    }

    public String getSpecificGoalDetails() {
        return specificGoalDetails;
    }

    public void setSpecificGoalDetails(String specificGoalDetails) {
        this.specificGoalDetails = specificGoalDetails;
    }
}