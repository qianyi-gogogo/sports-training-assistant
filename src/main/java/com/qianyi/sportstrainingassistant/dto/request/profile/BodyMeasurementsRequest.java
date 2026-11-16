
package com.qianyi.sportstrainingassistant.dto.request.profile;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class BodyMeasurementsRequest {

    // ==================== Chest ====================

    @DecimalMin("40.0")
    @DecimalMax("200.0")
    private Double chestCircumferenceCm;

    // ==================== Waist ====================

    @DecimalMin("30.0")
    @DecimalMax("200.0")
    private Double waistCircumferenceCm;

    // ==================== Left Arm ====================

    @DecimalMin("10.0")
    @DecimalMax("80.0")
    private Double leftArmCircumferenceCm;

    // ==================== Right Arm ====================

    @DecimalMin("10.0")
    @DecimalMax("80.0")
    private Double rightArmCircumferenceCm;

    // ==================== Thigh ====================

    @DecimalMin("20.0")
    @DecimalMax("120.0")
    private Double thighCircumferenceCm;


    // ==================== Getters & Setters ====================

    public Double getChestCircumferenceCm() {
        return chestCircumferenceCm;
    }

    public void setChestCircumferenceCm(
            Double chestCircumferenceCm
    ) {
        this.chestCircumferenceCm = chestCircumferenceCm;
    }

    public Double getWaistCircumferenceCm() {
        return waistCircumferenceCm;
    }

    public void setWaistCircumferenceCm(
            Double waistCircumferenceCm
    ) {
        this.waistCircumferenceCm = waistCircumferenceCm;
    }

    public Double getLeftArmCircumferenceCm() {
        return leftArmCircumferenceCm;
    }

    public void setLeftArmCircumferenceCm(
            Double leftArmCircumferenceCm
    ) {
        this.leftArmCircumferenceCm = leftArmCircumferenceCm;
    }

    public Double getRightArmCircumferenceCm() {
        return rightArmCircumferenceCm;
    }

    public void setRightArmCircumferenceCm(
            Double rightArmCircumferenceCm
    ) {
        this.rightArmCircumferenceCm = rightArmCircumferenceCm;
    }

    public Double getThighCircumferenceCm() {
        return thighCircumferenceCm;
    }

    public void setThighCircumferenceCm(
            Double thighCircumferenceCm
    ) {
        this.thighCircumferenceCm = thighCircumferenceCm;
    }
}
