
package com.qianyi.sportstrainingassistant.entity;

import com.qianyi.sportstrainingassistant.model.TrainingGoal;
import com.qianyi.sportstrainingassistant.model.TrainingGoalType;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "training_goals",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_goal_version",
                columnNames = {"user_id", "version"}
        )
)
public class TrainingGoalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "version", nullable = false)
    private Integer version;

    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "specific_goal_details",
            columnDefinition = "TEXT"
    )
    private String specificGoalDetails;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "training_goal_priorities",
            joinColumns = @JoinColumn(
                    name = "training_goal_id",
                    nullable = false
            )
    )
    @OrderColumn(
            name = "priority_index",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    @Column(
            name = "goal_type",
            nullable = false,
            length = 40
    )
    private List<TrainingGoalType> goalPriorities =
            new ArrayList<>();


    // Required by JPA.
    protected TrainingGoalEntity() {
    }

    // Default to version 1 for an initial goal.
    public TrainingGoalEntity(
            Long userId,
            TrainingGoal goal
    ) {
        this(userId, 1, goal);
    }

    // Create a specific version of a training goal.
    public TrainingGoalEntity(
            Long userId,
            Integer version,
            TrainingGoal goal
    ) {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException(
                    "User ID must be positive"
            );
        }

        if (version == null || version <= 0) {
            throw new IllegalArgumentException(
                    "Version must be positive"
            );
        }

        this.userId = userId;
        this.version = version;

        updateFromModel(goal);
    }

    // Copy data from a validated model.
    private  void updateFromModel(TrainingGoal goal) {
        if (goal == null) {
            throw new IllegalArgumentException(
                    "Training goal cannot be null"
            );
        }

        this.goalPriorities.clear();
        this.goalPriorities.addAll(
                goal.getGoalPriorities()
        );

        this.specificGoalDetails =
                goal.getSpecificGoalDetails();
    }

    // Convert the database entity to the domain model.
    public TrainingGoal toModel() {
        return new TrainingGoal(
                goalPriorities,
                specificGoalDetails
        );
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Integer getVersion() {
        return version;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public List<TrainingGoalType> getGoalPriorities() {
        return List.copyOf(goalPriorities);
    }

    public String getSpecificGoalDetails() {
        return specificGoalDetails;
    }
}
