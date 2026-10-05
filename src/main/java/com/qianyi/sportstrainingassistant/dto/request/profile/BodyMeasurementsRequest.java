package com.qianyi.sportstrainingassistant.dto.request.profile;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public class BodyMeasurementsRequest {

    @DecimalMin("40.0")
    @DecimalMax("200.0")
    private Double chestCircumferenceCm;

    @DecimalMin("30.0")
    @DecimalMax("200.0")
    private Double waistCircumferenceCm;

    @DecimalMin("10.0")
    @DecimalMax("80.0")
    private Double armCircumferenceCm;

    @DecimalMin("20.0")
    @DecimalMax("120.0")
    private Double thighCircumferenceCm;


    public Double getChestCircumferenceCm() {
        return chestCircumferenceCm;
    }

    public void setChestCircumferenceCm(Double chestCircumferenceCm) {
        this.chestCircumferenceCm = chestCircumferenceCm;
    }

    public Double getWaistCircumferenceCm() {
        return waistCircumferenceCm;
    }

    public void setWaistCircumferenceCm(Double waistCircumferenceCm) {
        this.waistCircumferenceCm = waistCircumferenceCm;
    }

    public Double getArmCircumferenceCm() {
        return armCircumferenceCm;
    }

    public void setArmCircumferenceCm(Double armCircumferenceCm) {
        this.armCircumferenceCm = armCircumferenceCm;
    }

    public Double getThighCircumferenceCm() {
        return thighCircumferenceCm;
    }

    public void setThighCircumferenceCm(Double thighCircumferenceCm) {
        this.thighCircumferenceCm = thighCircumferenceCm;
    }
}