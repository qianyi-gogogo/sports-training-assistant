
package com.qianyi.sportstrainingassistant.service;

import com.qianyi.sportstrainingassistant.dto.request.goal.TrainingGoalRequest;
import com.qianyi.sportstrainingassistant.entity.TrainingGoalEntity;
import com.qianyi.sportstrainingassistant.model.TrainingGoal;
import com.qianyi.sportstrainingassistant.repository.TrainingGoalRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import com.qianyi.sportstrainingassistant.dto.response.goal.TrainingGoalResponse;

@Service
public class TrainingGoalService {

    private final TrainingGoalRepository repository;

    public TrainingGoalService(
            TrainingGoalRepository repository
    ) {
        this.repository = repository;
    }


    // Create the first goal version for a user.
    @Transactional
    public TrainingGoalResponse createGoal(
            Long userId,
            TrainingGoalRequest request
    ) {
        validateUserId(userId);

        if (repository.existsByUserId(userId)) {
            throw new IllegalStateException(
                    "User already has a training goal"
            );
        }

        TrainingGoal goal = toModel(request);

        TrainingGoalEntity entity =
                new TrainingGoalEntity(userId, 1, goal);

        TrainingGoalEntity saved =
                repository.saveAndFlush(entity);

        return TrainingGoalResponse.fromEntity(saved);
    }


    // Get the latest goal version.
    @Transactional(readOnly = true)
    public TrainingGoal getCurrentGoal(Long userId) {
        validateUserId(userId);

        TrainingGoalEntity entity = repository
                .findFirstByUserIdOrderByVersionDesc(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Training goal not found"
                        )
                );

        return entity.toModel();
    }


    // Create a new version without overwriting history.
    @Transactional
    public TrainingGoalResponse updateGoal(
            Long userId,
            TrainingGoalRequest request
    ) {
        validateUserId(userId);

        TrainingGoalEntity current = repository
                .findFirstByUserIdOrderByVersionDesc(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Training goal not found"
                        )
                );

        TrainingGoal updatedGoal = toModel(request);

        int nextVersion = Math.addExact(
                current.getVersion(), 1
        );

        TrainingGoalEntity newVersion =
                new TrainingGoalEntity(
                        userId,
                        nextVersion,
                        updatedGoal
                );

        TrainingGoalEntity saved =
                repository.saveAndFlush(newVersion);

        return TrainingGoalResponse.fromEntity(saved);
    }

    // Get all goal versions, newest first.
    @Transactional(readOnly = true)
    public List<TrainingGoal> getGoalHistory(Long userId) {
        validateUserId(userId);

        return repository
                .findByUserIdOrderByVersionDesc(userId)
                .stream()
                .map(TrainingGoalEntity::toModel)
                .toList();
    }

    // Get the latest goal with its version metadata.
    @Transactional(readOnly = true)
    public TrainingGoalResponse getCurrentGoalResponse(
            Long userId
    ) {
        validateUserId(userId);

        TrainingGoalEntity entity = repository
                .findFirstByUserIdOrderByVersionDesc(userId)
                .orElseThrow(() ->
                        new NoSuchElementException(
                                "Training goal not found"
                        )
                );

        return TrainingGoalResponse.fromEntity(entity);
    }

    // Get all versions with metadata, newest first.
    @Transactional(readOnly = true)
    public List<TrainingGoalResponse> getGoalHistoryResponses(
            Long userId
    ) {
        validateUserId(userId);

        return repository
                .findByUserIdOrderByVersionDesc(userId)
                .stream()
                .map(TrainingGoalResponse::fromEntity)
                .toList();
    }

    private TrainingGoal toModel(
            TrainingGoalRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Training goal request cannot be null"
            );
        }

        return new TrainingGoal(
                request.getGoalPriorities(),
                request.getSpecificGoalDetails()
        );
    }

    private void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be positive"
            );
        }
    }
}
