package com.qianyi.sportstrainingassistant.model;

public class BodyMeasurements {

    private Double chestCircumferenceCm;
    private Double waistCircumferenceCm;
    // Arm circumference: flexed and unpumped.
    private Double leftArmCircumferenceCm;
    private Double rightArmCircumferenceCm;
    private Double thighCircumferenceCm;


    // ==================== Chest ====================

    public Double getChestCircumferenceCm() {
        return chestCircumferenceCm;
    }

    public void setChestCircumferenceCm(Double chestCircumferenceCm) {

        if (chestCircumferenceCm != null
                && (!Double.isFinite(chestCircumferenceCm)
                || chestCircumferenceCm < 40
                || chestCircumferenceCm > 200)) {

            throw new IllegalArgumentException(
                    "Chest circumference must be between 40 and 200 cm"
            );
        }

        this.chestCircumferenceCm = chestCircumferenceCm;
    }


    // ==================== Waist ====================

    public Double getWaistCircumferenceCm() {
        return waistCircumferenceCm;
    }

    public void setWaistCircumferenceCm(Double waistCircumferenceCm) {

        if (waistCircumferenceCm != null
                && (!Double.isFinite(waistCircumferenceCm)
                || waistCircumferenceCm < 30
                || waistCircumferenceCm > 200)) {

            throw new IllegalArgumentException(
                    "Waist circumference must be between 30 and 200 cm"
            );
        }

        this.waistCircumferenceCm = waistCircumferenceCm;
    }



    // ==================== Left Arm ====================
    // Measured flexed, without a workout pump.

    public Double getLeftArmCircumferenceCm() {
        return leftArmCircumferenceCm;
    }

    public void setLeftArmCircumferenceCm(Double leftArmCircumferenceCm) {

        if (leftArmCircumferenceCm != null
                && (!Double.isFinite(leftArmCircumferenceCm)
                || leftArmCircumferenceCm < 10
                || leftArmCircumferenceCm > 80)) {

            throw new IllegalArgumentException(
                    "Left arm circumference must be between 10 and 80 cm"
            );
        }

        this.leftArmCircumferenceCm = leftArmCircumferenceCm;
    }


    // ==================== Right Arm ====================
    // Measured flexed, without a workout pump.

    public Double getRightArmCircumferenceCm() {
        return rightArmCircumferenceCm;
    }

    public void setRightArmCircumferenceCm(Double rightArmCircumferenceCm) {

        if (rightArmCircumferenceCm != null
                && (!Double.isFinite(rightArmCircumferenceCm)
                || rightArmCircumferenceCm < 10
                || rightArmCircumferenceCm > 80)) {

            throw new IllegalArgumentException(
                    "Right arm circumference must be between 10 and 80 cm"
            );
        }

        this.rightArmCircumferenceCm = rightArmCircumferenceCm;
    }



    // ==================== Thigh ====================

    public Double getThighCircumferenceCm() {
        return thighCircumferenceCm;
    }

    public void setThighCircumferenceCm(Double thighCircumferenceCm) {

        if (thighCircumferenceCm != null
                && (!Double.isFinite(thighCircumferenceCm)
                || thighCircumferenceCm < 20
                || thighCircumferenceCm > 120)) {

            throw new IllegalArgumentException(
                    "Thigh circumference must be between 20 and 120 cm"
            );
        }

        this.thighCircumferenceCm = thighCircumferenceCm;
    }
}