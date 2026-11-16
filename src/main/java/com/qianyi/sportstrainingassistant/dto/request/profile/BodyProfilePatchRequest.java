
package com.qianyi.sportstrainingassistant.dto.request.profile;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.AssertTrue;

public class BodyProfilePatchRequest {

    @DecimalMin("10.0")
    @DecimalMax("300.0")
    private Double weightKg;

    @DecimalMin("30.0")
    @DecimalMax("200.0")
    private Double waistCircumferenceCm;

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public Double getWaistCircumferenceCm() {
        return waistCircumferenceCm;
    }

    public void setWaistCircumferenceCm(Double waistCircumferenceCm) {
        this.waistCircumferenceCm = waistCircumferenceCm;
    }

    @AssertTrue(message = "At least one field must be provided")
    public boolean isUpdateProvided() {
        return weightKg != null
                || waistCircumferenceCm != null;
    }
}
