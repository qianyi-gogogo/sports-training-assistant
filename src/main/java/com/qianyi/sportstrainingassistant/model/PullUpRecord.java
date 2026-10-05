package com.qianyi.sportstrainingassistant.model;

public class PullUpRecord {

    private final PullUpMode mode;
    private final double weightKg;
    private final int reps;
    private final Integer rir;


    public PullUpRecord(
            PullUpMode mode,
            double weightKg,
            int reps,
            Integer rir) {

        validateMode(mode);
        validateWeight(mode, weightKg);
        validateReps(reps);
        validateRir(rir);

        this.mode = mode;
        this.weightKg = weightKg;
        this.reps = reps;
        this.rir = rir;
    }


    public PullUpMode getMode() {
        return mode;
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


    // ==================== Mode Validation ====================

    private void validateMode(PullUpMode mode) {

        if (mode == null) {
            throw new IllegalArgumentException(
                    "Pull-up mode cannot be null"
            );
        }
    }


    // ==================== Weight Validation ====================

    private void validateWeight(
            PullUpMode mode,
            double weightKg) {

        if (!Double.isFinite(weightKg)
                || weightKg < 0
                || weightKg > 500) {

            throw new IllegalArgumentException(
                    "Weight must be between 0 and 500 kg"
            );
        }

        if (mode == PullUpMode.BODYWEIGHT && weightKg != 0) {
            throw new IllegalArgumentException(
                    "Bodyweight pull-up weight must be 0"
            );
        }

        if ((mode == PullUpMode.WEIGHTED
                || mode == PullUpMode.ASSISTED)
                && weightKg <= 0) {

            throw new IllegalArgumentException(
                    "Weighted or assisted pull-up weight must be greater than 0"
            );
        }
    }


    // ==================== Reps Validation ====================

    private void validateReps(int reps) {

        if (reps <= 0) {
            throw new IllegalArgumentException(
                    "Reps must be greater than 0"
            );
        }
    }


    // ==================== RIR Validation ====================

    private void validateRir(Integer rir) {

        if (rir != null && (rir < 0 || rir > 5)) {
            throw new IllegalArgumentException(
                    "RIR must be between 0 and 5"
            );
        }
    }
}