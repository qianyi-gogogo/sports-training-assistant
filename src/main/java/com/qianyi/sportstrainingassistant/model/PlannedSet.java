package com.qianyi.sportstrainingassistant.model;

    public class PlannedSet {

        private SetType setType;
        private Double weightKg;
        private Integer minReps;
        private Integer maxReps;

        public PlannedSet(
                SetType setType,
                Double weightKg,
                Integer minReps,
                Integer maxReps) {

            if (setType == null) {
                throw new IllegalArgumentException(
                        "Set type cannot be null"
                );
            }

            if (weightKg == null || weightKg < -500 || weightKg > 500) {
                throw new IllegalArgumentException(
                        "Weight must be between -500 and 500 kg"
                );
            }

            if (minReps == null || minReps < 1) {
                throw new IllegalArgumentException(
                        "Minimum reps must be at least 1"
                );
            }

            if (maxReps == null || maxReps < minReps) {
                throw new IllegalArgumentException(
                        "Maximum reps must be greater than or equal to minimum reps"
                );
            }

            this.setType = setType;
            this.weightKg = weightKg;
            this.minReps = minReps;
            this.maxReps = maxReps;
        }

        public SetType getSetType() {
            return setType;
        }

        public Double getWeightKg() {
            return weightKg;
        }

        public Integer getMinReps() {
            return minReps;
        }

        public Integer getMaxReps() {
            return maxReps;
        }
    }
