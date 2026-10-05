package com.qianyi.sportstrainingassistant.dto.request.plan;

import com.qianyi.sportstrainingassistant.model.SetType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PlannedSetRequest {

    @NotNull
    private SetType setType;

    @NotNull
    @DecimalMin("-500.0")
    @DecimalMax("500.0")
    private Double weightKg;

    @NotNull
    @Min(1)
    private Integer minReps;

    @NotNull
    @Min(1)
    private Integer maxReps;


    public SetType getSetType() {
        return setType;
    }

    public void setSetType(SetType setType) {
        this.setType = setType;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public Integer getMinReps() {
        return minReps;
    }

    public void setMinReps(Integer minReps) {
        this.minReps = minReps;
    }

    public Integer getMaxReps() {
        return maxReps;
    }

    public void setMaxReps(Integer maxReps) {
        this.maxReps = maxReps;
    }
}