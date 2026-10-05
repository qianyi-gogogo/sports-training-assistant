package com.qianyi.sportstrainingassistant.dto.request.profile;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class StrengthRecordRequest {

    @NotNull
    @DecimalMin("0.0")
    @DecimalMax("500.0")
    private Double weightKg;

    @NotNull
    @Min(1)
    private Integer reps;

    @Min(0)
    @Max(5)
    private Integer rir;


    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }

    public Integer getRir() {
        return rir;
    }

    public void setRir(Integer rir) {
        this.rir = rir;
    }
}