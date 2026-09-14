package com.qianyi.sportstrainingassistant.model;

public class CompletedSet {

    private SetType setType;
    private Double weightKg;
    private Integer reps;
    private Integer rir;

    // Set type
    public SetType getSetType() {
        return setType;
    }

    public void setSetType(SetType setType) {
        if (setType == null) {
            throw new IllegalArgumentException("Set type cannot be null");
        }

        this.setType = setType;
    }

    // Weight
    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        if (weightKg == null) {
            throw new IllegalArgumentException("Weight cannot be null");
        }

        if (weightKg < -500 || weightKg > 500) {
            throw new IllegalArgumentException(
                    "Weight must be between -500 and 500 kg"
            );
        }

        this.weightKg = weightKg;
    }

    // Reps
    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        if (reps == null) {
            throw new IllegalArgumentException("Reps cannot be null");
        }

        if (reps <= 0) {
            throw new IllegalArgumentException(
                    "Reps must be greater than 0"
            );
        }

        this.reps = reps;
    }

    // RIR
    public Integer getRir() {
        return rir;
    }

    public void setRir(Integer rir) {
        if (rir != null && (rir < 0 || rir > 5)) {
            throw new IllegalArgumentException(
                    "RIR must be between 0 and 5"
            );
        }

        this.rir = rir;
    }
}
