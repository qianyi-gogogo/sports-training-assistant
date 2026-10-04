package com.qianyi.sportstrainingassistant.model;

public class CompletedSet {

    private final SetType setType;
    private final double weightKg;
    private final int reps;
    private final Integer rir;


    public CompletedSet(
            SetType setType,
            double weightKg,
            int reps,
            Integer rir) {

        if (setType == null) {
            throw new IllegalArgumentException(
                    "Set type cannot be null"
            );
        }

        if (!Double.isFinite(weightKg)
                || weightKg < -500
                || weightKg > 500) {

            throw new IllegalArgumentException(
                    "Weight must be between -500 and 500 kg"
            );
        }

        if (reps <= 0) {
            throw new IllegalArgumentException(
                    "Reps must be greater than 0"
            );
        }

        if (rir != null && (rir < 0 || rir > 5)) {
            throw new IllegalArgumentException(
                    "RIR must be between 0 and 5"
            );
        }

        this.setType = setType;
        this.weightKg = weightKg;
        this.reps = reps;
        this.rir = rir;
    }


    public SetType getSetType() {
        return setType;
    }

    public double getWeightKg() {
        return weightKg;
    }

    public int getReps() {
        return reps;
    }

    public Integer getRir() {
        return rir;
    }
}