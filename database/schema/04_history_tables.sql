-- Baseline schema from 2026-11-24. Run in numeric order.
USE sports_training_assistant;

CREATE TABLE IF NOT EXISTS `body_profile_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `body_profile_id` bigint NOT NULL,
  `weight_kg` double NOT NULL,
  `waist_circumference_cm` double DEFAULT NULL,
  `recorded_at` datetime(6) NOT NULL,
  `body_fat_percentage` double DEFAULT NULL,
  `chest_circumference_cm` double DEFAULT NULL,
  `left_arm_circumference_cm` double DEFAULT NULL,
  `right_arm_circumference_cm` double DEFAULT NULL,
  `thigh_circumference_cm` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_body_history_profile_time` (`body_profile_id`,`recorded_at`),
  CONSTRAINT `fk_body_history_profile` FOREIGN KEY (`body_profile_id`) REFERENCES `body_profiles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `strength_history_snapshots` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `body_profile_id` bigint NOT NULL,
  `exercise_type` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `recorded_at` datetime(6) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_strength_history_profile_time` (`body_profile_id`,`recorded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `strength_history_records` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `snapshot_id` bigint NOT NULL,
  `record_index` int NOT NULL,
  `weight_kg` double NOT NULL,
  `reps` int NOT NULL,
  `rir` int DEFAULT NULL,
  `pull_up_mode` varchar(30) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_strength_snapshot_position` (`snapshot_id`,`record_index`),
  CONSTRAINT `fk_strength_history_snapshot` FOREIGN KEY (`snapshot_id`) REFERENCES `strength_history_snapshots` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
