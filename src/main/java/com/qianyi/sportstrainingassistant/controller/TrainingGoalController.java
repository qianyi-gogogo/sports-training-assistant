package com.qianyi.sportstrainingassistant.controller;


import com.qianyi.sportstrainingassistant.model.TrainingGoal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/goals")
public class TrainingGoalController {

    private TrainingGoal currentGoal;

    @PostMapping
    public TrainingGoal createGoal(@RequestBody TrainingGoal goal) {
        this.currentGoal = goal;
        return currentGoal;
    }

    @GetMapping("/current")
    public TrainingGoal getCurrentGoal() {
        return currentGoal;
    }
}
