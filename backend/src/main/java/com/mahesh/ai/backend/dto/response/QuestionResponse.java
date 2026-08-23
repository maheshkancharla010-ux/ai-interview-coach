package com.mahesh.ai.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class QuestionResponse {
    private Long id;
    private String questionText;
    private String answerText;
    private Long interviewId;
    private LocalDateTime createdAt;
}
