package com.qianyi.sportstrainingassistant.model;

public class BodyProfile {
    private double height;
    private double weight;
    private double bodyFatPercentage;
    private int age;
    private String biologicalSex;
    private double armCircumference;

    // ==================== Strength ====================

    // Chest
    private ExerciseStrength benchPress;

    // Back
    private ExerciseStrength barbellRow;

    // Shoulder
    private ExerciseStrength strictPress;
    private ExerciseStrength dumbbellShoulderPress;

    // Legs
    private ExerciseStrength squat;
    private ExerciseStrength legPress;

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        if (height < 50 || height > 250) {
            throw new IllegalArgumentException("Height must be between 50 and 250 cm");
        }

        this.height = height;
    }


    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight < 10 || weight > 300) {
            throw new IllegalArgumentException("Weight must be between 10 and 300 kg");
        }

        this.weight = weight;
    }


    public double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(double bodyFatPercentage) {
        if (bodyFatPercentage < 3 || bodyFatPercentage > 60) {
            throw new IllegalArgumentException(
                    "Body fat percentage must be between 3 and 60"
            );
        }

        this.bodyFatPercentage = bodyFatPercentage;
    }


    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 8 || age > 100) {
            throw new IllegalArgumentException("Age must be between 8 and 100");
        }

        this.age = age;
    }


    public String getBiologicalSex() {
        return biologicalSex;
    }

    public void setBiologicalSex(String biologicalSex) {

        if (biologicalSex == null) {
            throw new IllegalArgumentException(
                    "Biological sex cannot be null"
            );
        }

        if (!biologicalSex.equalsIgnoreCase("male")
                && !biologicalSex.equalsIgnoreCase("female")) {

            throw new IllegalArgumentException(
                    "Biological sex must be male or female"
            );
        }

        this.biologicalSex = biologicalSex.toLowerCase();
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
    // ==================== Bench Press ====================

    // Chest
    public ExerciseStrength getBenchPress() {
        return benchPress;
    }
    public void setBenchPress(ExerciseStrength benchPress) {
        this.benchPress = benchPress;
    }
    // ==================== Barbell Row ====================

    // Back

    public ExerciseStrength getBarbellRow() {
        return barbellRow;
    }

    public void setBarbellRow(ExerciseStrength barbellRow) {
        this.barbellRow = barbellRow;
    }
    // ==================== Strict Press ====================

    // Shoulder

    public ExerciseStrength getStrictPress() {
        return strictPress;
    }

    public void setStrictPress(ExerciseStrength strictPress) {
        this.strictPress = strictPress;
    }
    // ==================== Dumbbell Shoulder Press ====================

    // Shoulder

    public ExerciseStrength getDumbbellShoulderPress() {
        return dumbbellShoulderPress;
    }

    public void setDumbbellShoulderPress(ExerciseStrength dumbbellShoulderPress) {
        this.dumbbellShoulderPress = dumbbellShoulderPress;
    }
    // ==================== Squat ====================

    // Legs

    public ExerciseStrength getSquat() {
        return squat;
    }

    public void setSquat(ExerciseStrength squat) {
        this.squat = squat;
    }
    // ==================== Leg Press ====================

    // Legs

    public ExerciseStrength getLegPress() {
        return legPress;
    }

    public void setLegPress(ExerciseStrength legPress) {
        this.legPress = legPress;
    }


}



