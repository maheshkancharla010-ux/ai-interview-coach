package com.mahesh.ai.backend.mapper;

import com.mahesh.ai.backend.dto.response.FeedbackResponse;
import com.mahesh.ai.backend.entity.Feedback;

public class FeedbackMapper {

    public static FeedbackResponse toResponse(Feedback feedback) {
        if (feedback == null) {
            return null;
        }

        return FeedbackResponse.builder()
                .id(feedback.getId())
                .feedbackText(feedback.getFeedbackText())
                .score(feedback.getScore())
                .interviewId(feedback.getInterview() != null ? feedback.getInterview().getId() : null)
                .questionId(feedback.getQuestion() != null ? feedback.getQuestion().getId() : null)
                .createdAt(feedback.getCreatedAt())
                .build();
    }
}
