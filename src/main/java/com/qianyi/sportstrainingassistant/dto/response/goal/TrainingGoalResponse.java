
package com.qianyi.sportstrainingassistant.dto.response.goal;

import com.qianyi.sportstrainingassistant.entity.TrainingGoalEntity;
import com.qianyi.sportstrainingassistant.model.TrainingGoalType;

import java.time.LocalDateTime;
import java.util.List;

public record TrainingGoalResponse(
        Integer version,
        LocalDateTime createdAt,
        List<TrainingGoalType> goalPriorities,
        String specificGoalDetails
) {

    public TrainingGoalResponse {
        goalPriorities = List.copyOf(goalPriorities);
    }

    // Convert a database entity into an API response.
    public static TrainingGoalResponse fromEntity(
            TrainingGoalEntity entity
    ) {
        if (entity == null) {
            throw new IllegalArgumentException(
                    "Training goal entity cannot be null"
            );
        }

        return new TrainingGoalResponse(
                entity.getVersion(),
                entity.getCreatedAt(),
                entity.getGoalPriorities(),
                entity.getSpecificGoalDetails()
        );
    }
}
