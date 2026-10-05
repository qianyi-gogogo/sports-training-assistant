package com.qianyi.sportstrainingassistant.dto.request.plan;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public class WeeklyTrainingPlanRequest {

    @NotNull
    @Min(1)
    private Integer weekNumber;

    @NotNull
    private LocalDate startDate;

    @NotNull
    @Size(min = 7, max = 7)
    @Valid
    private List<TrainingDayPlanRequest> days;


    public Integer getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(Integer weekNumber) {
        this.weekNumber = weekNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<TrainingDayPlanRequest> getDays() {
        return days;
    }

    public void setDays(List<TrainingDayPlanRequest> days) {
        this.days = days;
    }
}