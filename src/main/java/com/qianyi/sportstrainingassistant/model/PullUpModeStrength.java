package com.qianyi.sportstrainingassistant.model;

public class PullUpModeStrength {
    private PullUpRecord oneToTwo;
    private PullUpRecord fourToSix;
    private PullUpRecord tenToTwelve;


    public PullUpRecord getOneToTwo() {
        return oneToTwo;
    }

    public void setOneToTwo(PullUpRecord record) {
        validateRepRange(record, 1, 2);
        this.oneToTwo = record;
    }


    public PullUpRecord getFourToSix() {
        return fourToSix;
    }

    public void setFourToSix(PullUpRecord record) {
        validateRepRange(record, 4, 6);
        this.fourToSix = record;
    }


    public PullUpRecord getTenToTwelve() {
        return tenToTwelve;
    }

    public void setTenToTwelve(PullUpRecord record) {
        validateRepRange(record, 10, 12);
        this.tenToTwelve = record;
    }


    private void validateRepRange(
            PullUpRecord record,
            int minReps,
            int maxReps) {

        if (record == null) {
            return;
        }

        int reps = record.getReps();

        if (reps < minReps || reps > maxReps) {
            throw new IllegalArgumentException(
                    "Reps must be between "
                            + minReps
                            + " and "
                            + maxReps
            );
        }
    }
}
