
package com.qianyi.sportstrainingassistant.dto.request.profile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class BodyProfileRequest {

    @NotNull
    @Valid
    private BasicBodyInfoRequest basicBodyInfo;

    @Valid
    private BodyMeasurementsRequest bodyMeasurements;

    @Valid
    private StrengthProfileRequest strengthProfile;


    // ==================== Basic Body Info ====================

    public BasicBodyInfoRequest getBasicBodyInfo() {
        return basicBodyInfo;
    }

    public void setBasicBodyInfo(
            BasicBodyInfoRequest basicBodyInfo
    ) {
        this.basicBodyInfo = basicBodyInfo;
    }


    // ==================== Body Measurements ====================

    public BodyMeasurementsRequest getBodyMeasurements() {
        return bodyMeasurements;
    }

    public void setBodyMeasurements(
            BodyMeasurementsRequest bodyMeasurements
    ) {
        this.bodyMeasurements = bodyMeasurements;
    }


    // ==================== Strength Profile ====================

    public StrengthProfileRequest getStrengthProfile() {
        return strengthProfile;
    }

    public void setStrengthProfile(
            StrengthProfileRequest strengthProfile
    ) {
        this.strengthProfile = strengthProfile;
    }
}
