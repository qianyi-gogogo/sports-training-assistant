package com.qianyi.sportstrainingassistant.dto.request.plan;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ExercisePlanRequest {

    @NotBlank
    private String exerciseName;

    @NotNull
    @Size(min = 1)
    @Valid
    private List<PlannedSetRequest> sets;


    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public List<PlannedSetRequest> getSets() {
        return sets;
    }

    public void setSets(List<PlannedSetRequest> sets) {
        this.sets = sets;
    }
}