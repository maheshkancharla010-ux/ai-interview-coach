package com.mahesh.ai.backend.service;

import com.mahesh.ai.backend.dto.response.FeedbackResult;

import java.util.List;

public interface AiService {

    List<String> generateQuestions(String jobTitle, String jobDescription, String resumeContent);

    FeedbackResult generateFeedback(String questionText, String answerText, String jobTitle);
}
