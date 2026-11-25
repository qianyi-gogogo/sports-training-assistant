-- Baseline schema from 2026-11-24. Run in numeric order.
USE sports_training_assistant;

CREATE TABLE IF NOT EXISTS `body_profiles` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `height_cm` double NOT NULL,
  `weight_kg` double NOT NULL,
  `body_fat_percentage` double DEFAULT NULL,
  `age` int NOT NULL,
  `biological_sex` varchar(20) NOT NULL,
  `chest_circumference_cm` double DEFAULT NULL,
  `waist_circumference_cm` double DEFAULT NULL,
  `left_arm_circumference_cm` double DEFAULT NULL,
  `right_arm_circumference_cm` double DEFAULT NULL,
  `thigh_circumference_cm` double DEFAULT NULL,
  `created_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `updated_at` datetime(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_body_profile_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
