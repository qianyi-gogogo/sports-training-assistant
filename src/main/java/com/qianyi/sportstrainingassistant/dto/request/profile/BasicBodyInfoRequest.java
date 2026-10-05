package com.qianyi.sportstrainingassistant.dto.request.profile;

import com.qianyi.sportstrainingassistant.model.BiologicalSex;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class BasicBodyInfoRequest {

    @NotNull
    @DecimalMin("50.0")
    @DecimalMax("250.0")
    private Double heightCm;

    @NotNull
    @DecimalMin("10.0")
    @DecimalMax("300.0")
    private Double weightKg;

    @DecimalMin("3.0")
    @DecimalMax("60.0")
    private Double bodyFatPercentage;

    @NotNull
    @Min(8)
    @Max(100)
    private Integer age;

    @NotNull
    private BiologicalSex biologicalSex;


    public Double getHeightCm() {
        return heightCm;
    }

    public void setHeightCm(Double heightCm) {
        this.heightCm = heightCm;
    }

    public Double getWeightKg() {
        return weightKg;
    }

    public void setWeightKg(Double weightKg) {
        this.weightKg = weightKg;
    }

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public BiologicalSex getBiologicalSex() {
        return biologicalSex;
    }

    public void setBiologicalSex(BiologicalSex biologicalSex) {
        this.biologicalSex = biologicalSex;
    }
}