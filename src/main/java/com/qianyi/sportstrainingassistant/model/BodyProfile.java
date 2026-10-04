package com.qianyi.sportstrainingassistant.model;

public class BodyProfile {

    private BasicBodyInfo basicBodyInfo;
    private BodyMeasurements bodyMeasurements;
    private StrengthProfile strengthProfile;


    public BodyProfile(BasicBodyInfo basicBodyInfo) {
        setBasicBodyInfo(basicBodyInfo);
    }


    // ==================== Basic Body Info ====================

    public BasicBodyInfo getBasicBodyInfo() {
        return basicBodyInfo;
    }

    public void setBasicBodyInfo(BasicBodyInfo basicBodyInfo) {

        if (basicBodyInfo == null) {
            throw new IllegalArgumentException(
                    "Basic body info cannot be null"
            );
        }

        this.basicBodyInfo = basicBodyInfo;
    }


    // ==================== Body Measurements ====================

    public BodyMeasurements getBodyMeasurements() {
        return bodyMeasurements;
    }

    public void setBodyMeasurements(BodyMeasurements bodyMeasurements) {
        this.bodyMeasurements = bodyMeasurements;
    }


    // ==================== Strength Profile ====================

    public StrengthProfile getStrengthProfile() {
        return strengthProfile;
    }

    public void setStrengthProfile(StrengthProfile strengthProfile) {
        this.strengthProfile = strengthProfile;
    }
}









