package com.qianyi.sportstrainingassistant.dto.request.goal;

import com.qianyi.sportstrainingassistant.model.TrainingGoalType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class TrainingGoalRequest {

    @NotNull
    @Size(min = 5, max = 5)
    private List<@NotNull TrainingGoalType> goalPriorities;

    private String specificGoalDetails;


    public List<TrainingGoalType> getGoalPriorities() {
        return goalPriorities;
    }

    public void setGoalPriorities(List<TrainingGoalType> goalPriorities) {
        this.goalPriorities = goalPriorities;
    }

    public String getSpecificGoalDetails() {
        return specificGoalDetails;
    }

    public void setSpecificGoalDetails(String specificGoalDetails) {
        this.specificGoalDetails = specificGoalDetails;
    }
}