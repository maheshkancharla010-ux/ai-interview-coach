package com.mahesh.ai.backend.service;

import com.mahesh.ai.backend.dto.response.FeedbackResponse;
import com.mahesh.ai.backend.entity.Feedback;

import java.util.List;

public interface FeedbackService {
    FeedbackResponse generateFeedbackForQuestion(Long questionId, Long userId);
    List<FeedbackResponse> generateFeedbackForInterview(Long interviewId, Long userId);
    List<FeedbackResponse> getFeedbackByInterview(Long interviewId, Long userId);
    FeedbackResponse getFeedback(Long id, Long userId);
    Feedback getFeedbackEntity(Long id, Long userId);
}
