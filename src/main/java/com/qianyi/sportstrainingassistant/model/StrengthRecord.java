package com.qianyi.sportstrainingassistant.model;

public class StrengthRecord {

    private final double weightKg;
    private final int reps;
    private final Integer rir;


    public StrengthRecord(
            double weightKg,
            int reps,
            Integer rir) {

        if (!Double.isFinite(weightKg)
                || weightKg < 0
                || weightKg > 500) {

            throw new IllegalArgumentException(
                    "Weight must be between 0 and 500 kg"
            );
        }

        if (reps < 1) {
            throw new IllegalArgumentException(
                    "Reps must be at least 1"
            );
        }

        if (rir != null && (rir < 0 || rir > 5)) {
            throw new IllegalArgumentException(
                    "RIR must be between 0 and 5"
            );
        }

        this.weightKg = weightKg;
        this.reps = reps;
        this.rir = rir;
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