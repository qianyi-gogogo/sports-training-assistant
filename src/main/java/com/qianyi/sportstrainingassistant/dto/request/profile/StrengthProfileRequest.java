package com.qianyi.sportstrainingassistant.dto.request.profile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.util.List;

public class StrengthProfileRequest {

    @Valid
    @Size(min = 1)
    private List<StrengthRecordRequest> benchPress;

    @Valid
    @Size(min = 1)
    private List<StrengthRecordRequest> barbellRow;

    @Valid
    @Size(min = 1)
    private List<PullUpRecordRequest> pullUp;

    @Valid
    @Size(min = 1)
    private List<StrengthRecordRequest> strictPress;

    @Valid
    @Size(min = 1)
    private List<StrengthRecordRequest> dumbbellShoulderPress;

    @Valid
    @Size(min = 1)
    private List<StrengthRecordRequest> squat;

    @Valid
    @Size(min = 1)
    private List<StrengthRecordRequest> legPress;


    public List<StrengthRecordRequest> getBenchPress() {
        return benchPress;
    }

    public void setBenchPress(List<StrengthRecordRequest> benchPress) {
        this.benchPress = benchPress;
    }

    public List<StrengthRecordRequest> getBarbellRow() {
        return barbellRow;
    }

    public void setBarbellRow(List<StrengthRecordRequest> barbellRow) {
        this.barbellRow = barbellRow;
    }

    public List<PullUpRecordRequest> getPullUp() {
        return pullUp;
    }

    public void setPullUp(List<PullUpRecordRequest> pullUp) {
        this.pullUp = pullUp;
    }

    public List<StrengthRecordRequest> getStrictPress() {
        return strictPress;
    }

    public void setStrictPress(List<StrengthRecordRequest> strictPress) {
        this.strictPress = strictPress;
    }

    public List<StrengthRecordRequest> getDumbbellShoulderPress() {
        return dumbbellShoulderPress;
    }

    public void setDumbbellShoulderPress(
            List<StrengthRecordRequest> dumbbellShoulderPress) {
        this.dumbbellShoulderPress = dumbbellShoulderPress;
    }

    public List<StrengthRecordRequest> getSquat() {
        return squat;
    }

    public void setSquat(List<StrengthRecordRequest> squat) {
        this.squat = squat;
    }

    public List<StrengthRecordRequest> getLegPress() {
        return legPress;
    }

    public void setLegPress(List<StrengthRecordRequest> legPress) {
        this.legPress = legPress;
    }
}
