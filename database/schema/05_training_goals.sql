-- Baseline schema from 2026-11-24. Run in numeric order.
USE sports_training_assistant;

CREATE TABLE IF NOT EXISTS `training_goals` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `specific_goal_details` text,
  `version` int NOT NULL DEFAULT '1',
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_goal_version` (`user_id`,`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `training_goal_priorities` (
  `training_goal_id` bigint NOT NULL,
  `priority_index` int NOT NULL,
  `goal_type` varchar(40) NOT NULL,
  PRIMARY KEY (`training_goal_id`,`priority_index`),
  CONSTRAINT `fk_goal_priorities` FOREIGN KEY (`training_goal_id`) REFERENCES `training_goals` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
