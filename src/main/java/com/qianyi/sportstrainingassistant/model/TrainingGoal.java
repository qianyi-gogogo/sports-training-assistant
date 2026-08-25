package com.qianyi.sportstrainingassistant.model;

import java.util.List;

public class TrainingGoal {

    private String goalType;
    private String targetResult;
    private List<String> focusAreas;
    private int targetDurationWeeks;
    private String description;

public TrainingGoal() {
}

public TrainingGoal(String goalType,
                    String targetResult,
                    List<String> focusAreas,
                    int targetDurationWeeks,
                    String description) {
    this.goalType = goalType;
    this.targetResult = targetResult;
    this.focusAreas = focusAreas;
    this.targetDurationWeeks = targetDurationWeeks;
    this.description = description;
}

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public String getTargetResult() {
        return targetResult;
    }

    public void setTargetResult(String targetResult) {
        this.targetResult = targetResult;
    }

    public List<String> getFocusAreas() {
        return focusAreas;
    }

    public void setFocusAreas(List<String> focusAreas) {
        this.focusAreas = focusAreas;
    }

    public int getTargetDurationWeeks() {
        return targetDurationWeeks;
    }

    public void setTargetDurationWeeks(int targetDurationWeeks) {
        this.targetDurationWeeks = targetDurationWeeks;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}