package com.qianyi.sportstrainingassistant.model;


    public class PullUpRecord {

        private double externalWeight;
        private int reps;
        private int rir;


        public double getExternalWeight() {
            return externalWeight;
        }

        public void setExternalWeight(double externalWeight) {
            if (externalWeight < 0) {
                throw new IllegalArgumentException(
                        "External weight cannot be negative"
                );
            }

            this.externalWeight = externalWeight;
        }


        public int getReps() {
            return reps;
        }

        public void setReps(int reps) {
            if (reps <= 0) {
                throw new IllegalArgumentException(
                        "Reps must be greater than 0"
                );
            }

            this.reps = reps;
        }


        public int getRir() {
            return rir;
        }

        public void setRir(int rir) {
            if (rir < 0 || rir > 5) {
                throw new IllegalArgumentException(
                        "RIR must be between 0 and 5"
                );
            }

            this.rir = rir;
        }
    }

