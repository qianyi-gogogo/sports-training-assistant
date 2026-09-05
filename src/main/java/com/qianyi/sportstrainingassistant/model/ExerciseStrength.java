package com.qianyi.sportstrainingassistant.model;

    public class ExerciseStrength {

        private StrengthRecord oneToTwo;
        private StrengthRecord fourToSix;
        private StrengthRecord tenToTwelve;
        // ==================== Weight Order Validation ====================

        /*
         * Lower-rep ranges should not use less weight than higher-rep ranges.
         *
         * Required:
         * 1-2 reps >= 4-6 reps >= 10-12 reps
         *
         * If lower-rep weight is smaller:
         * -> invalid data
         * -> throw IllegalArgumentException
         *
         * If weights are equal:
         * -> valid data
         * -> but needsRetest() should return true
         */
        private void validateRir(StrengthRecord record) {

            if (record != null && record.getRir() != null) {

                if (record.getRir() > 2) {
                    throw new IllegalArgumentException(
                            "RIR is too high for strength testing. Please retest with RIR 0-2."
                    );
                }
            }
        }


        // ==================== 1-2 Reps ====================

        public StrengthRecord getOneToTwo() {
            return oneToTwo;
        }

        public void setOneToTwo(StrengthRecord record) {

            // Reps check
            if (record != null && record.getReps() != null) {

                if (record.getReps() < 1 || record.getReps() > 2) {
                    throw new IllegalArgumentException(
                            "Reps must be between 1 and 2"
                    );
                }
            }

            // RIR check
            validateRir(record);

            // Weight order check
            if (record != null && record.getWeight() != null) {

                if (fourToSix != null
                        && fourToSix.getWeight() != null
                        && record.getWeight() < fourToSix.getWeight()) {

                    throw new IllegalArgumentException(
                            "1-2 rep weight cannot be lower than 4-6 rep weight"
                    );
                }

                if (tenToTwelve != null
                        && tenToTwelve.getWeight() != null
                        && record.getWeight() < tenToTwelve.getWeight()) {

                    throw new IllegalArgumentException(
                            "1-2 rep weight cannot be lower than 10-12 rep weight"
                    );
                }
            }

            this.oneToTwo = record;
        }


// ==================== 4-6 Reps ====================

        public StrengthRecord getFourToSix() {
            return fourToSix;
        }

        public void setFourToSix(StrengthRecord record) {

            // Reps check
            if (record != null && record.getReps() != null) {

                if (record.getReps() < 4 || record.getReps() > 6) {
                    throw new IllegalArgumentException(
                            "Reps must be between 4 and 6"
                    );
                }
            }

            // RIR check
            validateRir(record);

            // Weight order check
            if (record != null && record.getWeight() != null) {

                if (oneToTwo != null
                        && oneToTwo.getWeight() != null
                        && record.getWeight() > oneToTwo.getWeight()) {

                    throw new IllegalArgumentException(
                            "4-6 rep weight cannot be higher than 1-2 rep weight"
                    );
                }

                if (tenToTwelve != null
                        && tenToTwelve.getWeight() != null
                        && record.getWeight() < tenToTwelve.getWeight()) {

                    throw new IllegalArgumentException(
                            "4-6 rep weight cannot be lower than 10-12 rep weight"
                    );
                }
            }

            this.fourToSix = record;
        }


// ==================== 10-12 Reps ====================

        public StrengthRecord getTenToTwelve() {
            return tenToTwelve;
        }

        public void setTenToTwelve(StrengthRecord record) {

            // Reps check
            if (record != null && record.getReps() != null) {

                if (record.getReps() < 10 || record.getReps() > 12) {
                    throw new IllegalArgumentException(
                            "Reps must be between 10 and 12"
                    );
                }
            }

            // RIR check
            validateRir(record);

            // Weight order check
            if (record != null && record.getWeight() != null) {

                if (fourToSix != null
                        && fourToSix.getWeight() != null
                        && record.getWeight() > fourToSix.getWeight()) {

                    throw new IllegalArgumentException(
                            "10-12 rep weight cannot be higher than 4-6 rep weight"
                    );
                }

                if (oneToTwo != null
                        && oneToTwo.getWeight() != null
                        && record.getWeight() > oneToTwo.getWeight()) {

                    throw new IllegalArgumentException(
                            "10-12 rep weight cannot be higher than 1-2 rep weight"
                    );
                }
            }

            this.tenToTwelve = record;
        }


// ==================== Data Quality Check ====================

        /*
         * If adjacent rep ranges use the same weight,
         * the data is still valid but may be inaccurate.
         *
         * Example:
         * 1-2 reps = 80 kg
         * 4-6 reps = 80 kg
         *
         * This should NOT throw an exception.
         * Instead, recommend the user retest.
         */
        public boolean needsRetest() {

            return hasEqualWeight(oneToTwo, fourToSix)
                    || hasEqualWeight(fourToSix, tenToTwelve);
        }


        /*
         * Check whether two strength records use the same weight.
         *
         * null means the user has not provided that record yet,
         * so it should not trigger a retest recommendation.
         */
        private boolean hasEqualWeight(
                StrengthRecord first,
                StrengthRecord second) {

            if (first == null || second == null) {
                return false;
            }

            if (first.getWeight() == null || second.getWeight() == null) {
                return false;
            }

            return Double.compare(
                    first.getWeight(),
                    second.getWeight()
            ) == 0;
        }
    }



