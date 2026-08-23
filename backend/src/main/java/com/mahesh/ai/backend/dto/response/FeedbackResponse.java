package com.mahesh.ai.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class FeedbackResponse {
    private Long id;
    private String feedbackText;
    private Integer score;
    private Long interviewId;
    private Long questionId;
    private LocalDateTime createdAt;
}
