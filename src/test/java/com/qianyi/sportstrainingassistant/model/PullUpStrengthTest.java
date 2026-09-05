package com.qianyi.sportstrainingassistant.model;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

    public class PullUpStrengthTest {

        @Test
        void bodyweightShouldRejectExternalWeight() {

            PullUpRecord record = new PullUpRecord();
            record.setExternalWeight(10);
            record.setReps(5);
            record.setRir(1);

            PullUpModeStrength bodyweight = new PullUpModeStrength();
            bodyweight.setFourToSix(record);

            PullUpStrength pullUpStrength = new PullUpStrength();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> pullUpStrength.setBodyweight(bodyweight)
            );
        }

        @Test
        void weightedPullUpShouldAcceptCorrectWeightOrder() {

            // 1-2 reps: +25 kg
            PullUpRecord oneToTwoRecord = new PullUpRecord();
            oneToTwoRecord.setExternalWeight(25);
            oneToTwoRecord.setReps(2);
            oneToTwoRecord.setRir(1);

            // 4-6 reps: +15 kg
            PullUpRecord fourToSixRecord = new PullUpRecord();
            fourToSixRecord.setExternalWeight(15);
            fourToSixRecord.setReps(5);
            fourToSixRecord.setRir(1);

            // 10-12 reps: +5 kg
            PullUpRecord tenToTwelveRecord = new PullUpRecord();
            tenToTwelveRecord.setExternalWeight(5);
            tenToTwelveRecord.setReps(10);
            tenToTwelveRecord.setRir(1);

            PullUpModeStrength weighted = new PullUpModeStrength();

            weighted.setOneToTwo(oneToTwoRecord);
            weighted.setFourToSix(fourToSixRecord);
            weighted.setTenToTwelve(tenToTwelveRecord);

            PullUpStrength pullUpStrength = new PullUpStrength();

            assertDoesNotThrow(
                    () -> pullUpStrength.setWeighted(weighted)
            );
        }
        @Test
        void assistedPullUpShouldAcceptCorrectAssistanceOrder() {

            // 1-2 reps: 5 kg assistance
            PullUpRecord oneToTwoRecord = new PullUpRecord();
            oneToTwoRecord.setExternalWeight(5);
            oneToTwoRecord.setReps(2);
            oneToTwoRecord.setRir(1);

            // 4-6 reps: 15 kg assistance
            PullUpRecord fourToSixRecord = new PullUpRecord();
            fourToSixRecord.setExternalWeight(15);
            fourToSixRecord.setReps(5);
            fourToSixRecord.setRir(1);

            // 10-12 reps: 25 kg assistance
            PullUpRecord tenToTwelveRecord = new PullUpRecord();
            tenToTwelveRecord.setExternalWeight(25);
            tenToTwelveRecord.setReps(10);
            tenToTwelveRecord.setRir(1);

            PullUpModeStrength assisted = new PullUpModeStrength();

            assisted.setOneToTwo(oneToTwoRecord);
            assisted.setFourToSix(fourToSixRecord);
            assisted.setTenToTwelve(tenToTwelveRecord);

            PullUpStrength pullUpStrength = new PullUpStrength();

            assertDoesNotThrow(
                    () -> pullUpStrength.setAssisted(assisted)
            );
        }
        @Test
        void assistedPullUpShouldRejectWrongAssistanceOrder() {

            // 1-2 reps: 20 kg assistance
            PullUpRecord oneToTwoRecord = new PullUpRecord();
            oneToTwoRecord.setExternalWeight(20);
            oneToTwoRecord.setReps(2);
            oneToTwoRecord.setRir(1);

            // 4-6 reps: 10 kg assistance
            PullUpRecord fourToSixRecord = new PullUpRecord();
            fourToSixRecord.setExternalWeight(10);
            fourToSixRecord.setReps(5);
            fourToSixRecord.setRir(1);

            PullUpModeStrength assisted = new PullUpModeStrength();

            assisted.setOneToTwo(oneToTwoRecord);
            assisted.setFourToSix(fourToSixRecord);

            PullUpStrength pullUpStrength = new PullUpStrength();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> pullUpStrength.setAssisted(assisted)
            );
        }
        @Test
        void weightedPullUpShouldRejectZeroExternalWeight() {

            PullUpRecord record = new PullUpRecord();
            record.setExternalWeight(0);
            record.setReps(5);
            record.setRir(1);

            PullUpModeStrength weighted = new PullUpModeStrength();
            weighted.setFourToSix(record);

            PullUpStrength pullUpStrength = new PullUpStrength();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> pullUpStrength.setWeighted(weighted)
            );
        }
        @Test
        void assistedPullUpShouldRejectZeroExternalWeight() {

            PullUpRecord record = new PullUpRecord();
            record.setExternalWeight(0);
            record.setReps(5);
            record.setRir(1);

            PullUpModeStrength assisted = new PullUpModeStrength();
            assisted.setFourToSix(record);

            PullUpStrength pullUpStrength = new PullUpStrength();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> pullUpStrength.setAssisted(assisted)
            );
        }
    }
