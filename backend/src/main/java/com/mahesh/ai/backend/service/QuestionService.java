package com.mahesh.ai.backend.service;

import com.mahesh.ai.backend.dto.request.QuestionAnswerRequest;
import com.mahesh.ai.backend.dto.response.QuestionResponse;
import com.mahesh.ai.backend.entity.Question;

import java.util.List;

public interface QuestionService {
    List<QuestionResponse> generateQuestions(Long interviewId, Long userId);
    List<QuestionResponse> getQuestionsByInterview(Long interviewId, Long userId);
    QuestionResponse getQuestion(Long id, Long userId);
    QuestionResponse submitAnswer(Long id, QuestionAnswerRequest request, Long userId);
    Question getQuestionEntity(Long id, Long userId);
}
