
package com.qianyi.sportstrainingassistant.controller;

import com.qianyi.sportstrainingassistant.dto.request.goal.TrainingGoalRequest;
import com.qianyi.sportstrainingassistant.dto.response.goal.TrainingGoalResponse;
import com.qianyi.sportstrainingassistant.service.TrainingGoalService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users/{userId}/goals")
public class TrainingGoalController {

    private final TrainingGoalService service;

    public TrainingGoalController(
            TrainingGoalService service
    ) {
        this.service = service;
    }


    // Create the initial training goal.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingGoalResponse createGoal(
            @PathVariable Long userId,
            @Valid @RequestBody TrainingGoalRequest request
    ) {
        return service.createGoal(userId, request);
    }


    // Get the current training goal.
    @GetMapping("/current")
    public TrainingGoalResponse getCurrentGoal(
            @PathVariable Long userId
    ) {
        return service.getCurrentGoalResponse(userId);
    }


    // Create a new goal version and preserve history.
    @PostMapping("/versions")
    @ResponseStatus(HttpStatus.CREATED)
    public TrainingGoalResponse createNewVersion(
            @PathVariable Long userId,
            @Valid @RequestBody TrainingGoalRequest request
    ) {
        return service.updateGoal(userId, request);
    }


    // Get goal history, newest first.
    @GetMapping("/history")
    public List<TrainingGoalResponse> getGoalHistory(
            @PathVariable Long userId
    ) {
        return service.getGoalHistoryResponses(userId);
    }

}
