package com.mahesh.ai.backend.service.impl;

import com.mahesh.ai.backend.dto.response.FeedbackResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockAiServiceImplTest {

    private final MockAiServiceImpl aiService = new MockAiServiceImpl();

    @Test
    void generateQuestions_returnsDeterministicQuestions() {
        List<String> questions = aiService.generateQuestions(
                "Java Developer",
                "Spring Boot experience required",
                "5 years Java experience"
        );

        assertNotNull(questions);
        assertEquals(5, questions.size());
        assertTrue(questions.get(0).contains("Java Developer"));
    }

    @Test
    void generateQuestions_withoutOptionalFields() {
        List<String> questions = aiService.generateQuestions("DevOps Engineer", null, null);

        assertNotNull(questions);
        assertFalse(questions.isEmpty());
        assertTrue(questions.get(0).contains("DevOps Engineer"));
    }

    @Test
    void generateFeedback_shortAnswer_lowScore() {
        FeedbackResult result = aiService.generateFeedback(
                "Tell me about yourself",
                "Short",
                "Developer"
        );

        assertNotNull(result.getFeedbackText());
        assertEquals(30, result.getScore());
        assertTrue(result.getFeedbackText().contains("Mock AI Feedback"));
    }

    @Test
    void generateFeedback_detailedAnswer_highScore() {
        String longAnswer = "In my previous role at TechCorp, I led a team of five developers ".repeat(5);

        FeedbackResult result = aiService.generateFeedback(
                "Describe a challenging project",
                longAnswer,
                "Senior Developer"
        );

        assertTrue(result.getScore() >= 80);
        assertTrue(result.getFeedbackText().contains("Strong answer"));
    }

    @Test
    void generateFeedback_emptyAnswer_zeroScore() {
        FeedbackResult result = aiService.generateFeedback(
                "Why this role?",
                "",
                "Developer"
        );

        assertEquals(0, result.getScore());
        assertTrue(result.getFeedbackText().contains("No answer was provided"));
    }
}
