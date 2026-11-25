-- Baseline schema from 2026-11-24. Run in numeric order.
USE sports_training_assistant;

CREATE TABLE IF NOT EXISTS `exercise_strength_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `body_profile_id` bigint NOT NULL,
  `exercise_type` varchar(50) NOT NULL,
  `record_index` int NOT NULL,
  `weight_kg` double NOT NULL,
  `reps` int NOT NULL,
  `rir` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_exercise_strength_position` (`body_profile_id`,`exercise_type`,`record_index`),
  CONSTRAINT `fk_exercise_strength_profile` FOREIGN KEY (`body_profile_id`) REFERENCES `body_profiles` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `pull_up_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `body_profile_id` bigint NOT NULL,
  `mode` varchar(20) NOT NULL,
  `weight_kg` double NOT NULL,
  `reps` int NOT NULL,
  `rir` int NOT NULL,
  `record_index` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_pull_up_body_profile` (`body_profile_id`),
  CONSTRAINT `fk_pull_up_body_profile` FOREIGN KEY (`body_profile_id`) REFERENCES `body_profiles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
