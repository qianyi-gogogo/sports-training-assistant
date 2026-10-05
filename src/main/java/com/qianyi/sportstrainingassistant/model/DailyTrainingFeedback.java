package com.qianyi.sportstrainingassistant.model;

public class DailyTrainingFeedback {

    private final String feedbackText;

    public DailyTrainingFeedback(String feedbackText) {

        if (feedbackText == null || feedbackText.isBlank()) {
            this.feedbackText = null;
        } else {
            this.feedbackText = feedbackText.strip();
        }
    }

    public String getFeedbackText() {
        return feedbackText;
    }
}
