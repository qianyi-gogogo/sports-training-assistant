package com.qianyi.sportstrainingassistant.model;

public class BasicBodyInfo {

    private double heightCm;
    private double weightKg;
    private Double bodyFatPercentage;
    private int age;
    private BiologicalSex biologicalSex;


    public BasicBodyInfo(
            double heightCm,
            double weightKg,
            int age,
            BiologicalSex biologicalSex) {

        setHeightCm(heightCm);
        setWeightKg(weightKg);
        setAge(age);
        setBiologicalSex(biologicalSex);
    }


    // ==================== Height ====================

    public double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(double heightCm) {

        if (!Double.isFinite(heightCm)
                || heightCm < 50
                || heightCm > 250) {

            throw new IllegalArgumentException(
                    "Height must be between 50 and 250 cm"
            );
        }

        this.heightCm = heightCm;
    }


    // ==================== Weight ====================

    public double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(double weightKg) {

        if (!Double.isFinite(weightKg)
                || weightKg < 10
                || weightKg > 300) {

            throw new IllegalArgumentException(
                    "Weight must be between 10 and 300 kg"
            );
        }

        this.weightKg = weightKg;
    }


    // ==================== Body Fat Percentage ====================

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Double bodyFatPercentage) {

        if (bodyFatPercentage != null
                && (!Double.isFinite(bodyFatPercentage)
                || bodyFatPercentage < 3
                || bodyFatPercentage > 60)) {

            throw new IllegalArgumentException(
                    "Body fat percentage must be between 3 and 60"
            );
        }

        this.bodyFatPercentage = bodyFatPercentage;
    }


    // ==================== Age ====================

    public int getAge() {
        return age;
    }

    public void setAge(int age) {

        if (age < 8 || age > 100) {
            throw new IllegalArgumentException(
                    "Age must be between 8 and 100"
            );
        }

        this.age = age;
    }


    // ==================== Biological Sex ====================

    public BiologicalSex getBiologicalSex() {
        return biologicalSex;
    }

    public void setBiologicalSex(BiologicalSex biologicalSex) {

        if (biologicalSex == null) {
            throw new IllegalArgumentException(
                    "Biological sex cannot be null"
            );
        }

        this.biologicalSex = biologicalSex;
    }
}