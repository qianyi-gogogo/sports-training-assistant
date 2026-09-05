package com.qianyi.sportstrainingassistant.model;

public class StrengthProfile {
    // Chest
    private ExerciseStrength benchPress;

    // Back
    private ExerciseStrength barbellRow;
    private PullUpStrength pullUp;

    // Shoulder
    private ExerciseStrength strictPress;
    private ExerciseStrength dumbbellShoulderPress;

    // Legs
    private ExerciseStrength squat;
    private ExerciseStrength legPress;


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

    public PullUpStrength getPullUp() {
        return pullUp;
    }

    public void setPullUp(PullUpStrength pullUp) {
        this.pullUp = pullUp;
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
