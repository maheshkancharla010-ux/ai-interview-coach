package com.mahesh.ai.backend.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FeedbackResult {
    private String feedbackText;
    private int score;
}
