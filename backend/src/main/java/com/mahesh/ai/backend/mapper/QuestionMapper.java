package com.mahesh.ai.backend.mapper;

import com.mahesh.ai.backend.dto.response.QuestionResponse;
import com.mahesh.ai.backend.entity.Question;

public class QuestionMapper {

    public static QuestionResponse toResponse(Question question) {
        if (question == null) {
            return null;
        }

        return QuestionResponse.builder()
                .id(question.getId())
                .questionText(question.getQuestionText())
                .answerText(question.getAnswerText())
                .interviewId(question.getInterview() != null ? question.getInterview().getId() : null)
                .createdAt(question.getCreatedAt())
                .build();
    }
}
