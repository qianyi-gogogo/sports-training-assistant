package com.qianyi.sportstrainingassistant.model;

public class PullUpStrength {
    // Bodyweight pull-up
    private PullUpModeStrength bodyweight;

    // Weighted pull-up
    private PullUpModeStrength weighted;

    // Assisted pull-up
    private PullUpModeStrength assisted;


    // ==================== Bodyweight ====================

    public PullUpModeStrength getBodyweight() {
        return bodyweight;
    }

    public void setBodyweight(PullUpModeStrength bodyweight) {
        validateBodyweight(bodyweight);
        this.bodyweight = bodyweight;
    }


    // ==================== Weighted ====================

    public PullUpModeStrength getWeighted() {
        return weighted;
    }

    public void setWeighted(PullUpModeStrength weighted) {
        validateWeighted(weighted);
        this.weighted = weighted;
    }


    // ==================== Assisted ====================

    public PullUpModeStrength getAssisted() {
        return assisted;
    }

    public void setAssisted(PullUpModeStrength assisted) {
        validateAssisted(assisted);
        this.assisted = assisted;
    }


    // ==================== Bodyweight Validation ====================

    private void validateBodyweight(PullUpModeStrength bodyweight) {

        if (bodyweight == null) {
            return;
        }

        validateBodyweightRecord(bodyweight.getOneToTwo());
        validateBodyweightRecord(bodyweight.getFourToSix());
        validateBodyweightRecord(bodyweight.getTenToTwelve());
    }

    private void validateBodyweightRecord(PullUpRecord record) {

        if (record == null) {
            return;
        }

        if (record.getExternalWeight() != 0) {
            throw new IllegalArgumentException(
                    "Bodyweight pull-up external weight must be 0"
            );
        }
    }


    // ==================== Weighted Validation ====================

    private void validateWeighted(PullUpModeStrength weighted) {

        if (weighted == null) {
            return;
        }

        PullUpRecord oneToTwo = weighted.getOneToTwo();
        PullUpRecord fourToSix = weighted.getFourToSix();
        PullUpRecord tenToTwelve = weighted.getTenToTwelve();

        validatePositiveExternalWeight(oneToTwo);
        validatePositiveExternalWeight(fourToSix);
        validatePositiveExternalWeight(tenToTwelve);

        /*
         * Weighted pull-up:
         *
         * 1-2 reps >= 4-6 reps >= 10-12 reps
         */

        validateWeightedOrder(oneToTwo, fourToSix);
        validateWeightedOrder(fourToSix, tenToTwelve);
        validateWeightedOrder(oneToTwo, tenToTwelve);
    }

    private void validateWeightedOrder(
            PullUpRecord lowerRepRecord,
            PullUpRecord higherRepRecord) {

        if (lowerRepRecord == null || higherRepRecord == null) {
            return;
        }

        if (lowerRepRecord.getExternalWeight()
                < higherRepRecord.getExternalWeight()) {

            throw new IllegalArgumentException(
                    "Lower-rep weighted pull-up must not use less external weight"
            );
        }
    }


    // ==================== Assisted Validation ====================

    private void validateAssisted(PullUpModeStrength assisted) {

        if (assisted == null) {
            return;
        }

        PullUpRecord oneToTwo = assisted.getOneToTwo();
        PullUpRecord fourToSix = assisted.getFourToSix();
        PullUpRecord tenToTwelve = assisted.getTenToTwelve();

        validatePositiveExternalWeight(oneToTwo);
        validatePositiveExternalWeight(fourToSix);
        validatePositiveExternalWeight(tenToTwelve);

        /*
         * Assisted pull-up:
         *
         * Less assistance = harder
         *
         * 1-2 reps <= 4-6 reps <= 10-12 reps
         */

        validateAssistedOrder(oneToTwo, fourToSix);
        validateAssistedOrder(fourToSix, tenToTwelve);
        validateAssistedOrder(oneToTwo, tenToTwelve);
    }

    private void validateAssistedOrder(
            PullUpRecord lowerRepRecord,
            PullUpRecord higherRepRecord) {

        if (lowerRepRecord == null || higherRepRecord == null) {
            return;
        }

        if (lowerRepRecord.getExternalWeight()
                > higherRepRecord.getExternalWeight()) {

            throw new IllegalArgumentException(
                    "Lower-rep assisted pull-up must not use more assistance"
            );
        }
    }


    // ==================== Common Validation ====================

    private void validatePositiveExternalWeight(PullUpRecord record) {

        if (record == null) {
            return;
        }

        if (record.getExternalWeight() <= 0) {
            throw new IllegalArgumentException(
                    "External weight must be greater than 0"
            );
        }

    }

}
