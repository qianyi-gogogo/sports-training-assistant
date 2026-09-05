package com.qianyi.sportstrainingassistant.model;

public class BasicBodyInfo {
    private double height;
    private double weight;
    private double bodyFatPercentage;
    private int age;
    private String biologicalSex;

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
}
