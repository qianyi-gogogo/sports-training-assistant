package com.qianyi.sportstrainingassistant.model;

public class StrengthRecord {

    private Double weight;
    private Integer reps;
    private Integer rir;


    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        if (weight != null && (weight < 0 || weight > 1000)) {
            throw new IllegalArgumentException(
                    "Weight must be between 0 and 1000 kg"
            );
        }

        this.weight = weight;
    }


    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        if (reps != null && (reps < 1 || reps > 100)) {
            throw new IllegalArgumentException(
                    "Reps must be between 1 and 100"
            );
        }

        this.reps = reps;
    }

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
