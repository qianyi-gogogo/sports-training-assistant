package com.qianyi.sportstrainingassistant.model;

public class BodyMeasurements {
    private double chestCircumference;
    private double waistCircumference;
    private double armCircumference;
    private double thighCircumference;

    public double getChestCircumference() {
        return chestCircumference;
    }

    public void setChestCircumference(double chestCircumference) {
        if (chestCircumference < 40 || chestCircumference > 200) {
            throw new IllegalArgumentException(
                    "Chest circumference must be between 40 and 200 cm"
            );
        }

        this.chestCircumference = chestCircumference;
    }


    public double getWaistCircumference() {
        return waistCircumference;
    }

    public void setWaistCircumference(double waistCircumference) {
        if (waistCircumference < 30 || waistCircumference > 200) {
            throw new IllegalArgumentException(
                    "Waist circumference must be between 30 and 200 cm"
            );
        }

        this.waistCircumference = waistCircumference;
    }


    public double getArmCircumference() {
        return armCircumference;
    }

    public void setArmCircumference(double armCircumference) {
        if (armCircumference < 10 || armCircumference > 80) {
            throw new IllegalArgumentException(
                    "Arm circumference must be between 10 and 80 cm"
            );
        }

        this.armCircumference = armCircumference;
    }


    public double getThighCircumference() {
        return thighCircumference;
    }

    public void setThighCircumference(double thighCircumference) {
        if (thighCircumference < 20 || thighCircumference > 120) {
            throw new IllegalArgumentException(
                    "Thigh circumference must be between 20 and 120 cm"
            );
        }

        this.thighCircumference = thighCircumference;
    }
}
