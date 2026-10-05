package com.qianyi.sportstrainingassistant.dto.request.plan;

import com.qianyi.sportstrainingassistant.model.DayType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TrainingDayPlanRequest {

    private String workoutName;

    @NotNull
    private DayType dayType;

    @NotNull
    @Valid
    private List<ExercisePlanRequest> exercises;


    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }

    public List<ExercisePlanRequest> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExercisePlanRequest> exercises) {
        this.exercises = exercises;
    }
}