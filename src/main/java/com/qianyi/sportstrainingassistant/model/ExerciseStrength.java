package com.qianyi.sportstrainingassistant.model;

    public class ExerciseStrength {

        private StrengthRecord oneToTwo;
        private StrengthRecord fourToSix;
        private StrengthRecord tenToTwelve;

        private void validateWeightOrder() {

            if (oneToTwo != null
                    && fourToSix != null
                    && oneToTwo.getWeight() != null
                    && fourToSix.getWeight() != null) {

                if (oneToTwo.getWeight() < fourToSix.getWeight()) {
                    throw new IllegalArgumentException(
                            "1-2 rep weight cannot be lower than 4-6 rep weight"
                    );
                }
            }

            if (fourToSix != null
                    && tenToTwelve != null
                    && fourToSix.getWeight() != null
                    && tenToTwelve.getWeight() != null) {

                if (fourToSix.getWeight() < tenToTwelve.getWeight()) {
                    throw new IllegalArgumentException(
                            "4-6 rep weight cannot be lower than 10-12 rep weight"
                    );
                }
            }
        }
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

            if (record != null && record.getReps() != null) {

                if (record.getReps() < 1 || record.getReps() > 2) {
                    throw new IllegalArgumentException(
                            "Reps must be between 1 and 2"
                    );
                }
            }
            validateWeightOrder();

            validateRir(record);

            this.oneToTwo = record;
        }


        // ==================== 4-6 Reps ====================

        public StrengthRecord getFourToSix() {
            return fourToSix;
        }

        public void setFourToSix(StrengthRecord record) {

            if (record != null && record.getReps() != null) {

                if (record.getReps() < 4 || record.getReps() > 6) {
                    throw new IllegalArgumentException(
                            "Reps must be between 4 and 6"
                    );
                }
            }
            validateWeightOrder();

            validateRir(record);

            this.fourToSix = record;
        }


        // ==================== 10-12 Reps ====================

        public StrengthRecord getTenToTwelve() {
            return tenToTwelve;
        }

        public void setTenToTwelve(StrengthRecord record) {

            if (record != null && record.getReps() != null) {

                if (record.getReps() < 10 || record.getReps() > 12) {
                    throw new IllegalArgumentException(
                            "Reps must be between 10 and 12"
                    );
                }
            }
            validateWeightOrder();

            validateRir(record);

            this.tenToTwelve = record;
        }
    }

