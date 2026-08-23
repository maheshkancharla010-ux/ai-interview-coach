package com.mahesh.ai.backend.service.impl;

import com.mahesh.ai.backend.dto.response.FeedbackResult;
import com.mahesh.ai.backend.service.AiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockAiServiceImpl implements AiService {

    private static final List<String> BASE_QUESTIONS = List.of(
            "Tell me about yourself and your relevant experience for this role.",
            "Describe a challenging technical problem you solved recently.",
            "How do you approach designing scalable backend systems?",
            "Explain your experience with the technologies mentioned in the job description.",
            "Where do you see yourself contributing most in the first 90 days?"
    );

    @Override
    public List<String> generateQuestions(String jobTitle, String jobDescription, String resumeContent) {
        List<String> questions = new ArrayList<>();

        questions.add("Why are you interested in the " + jobTitle + " position?");

        if (jobDescription != null && !jobDescription.isBlank()) {
            questions.add("Based on the job requirements, how would you approach the key responsibilities described?");
        }

        if (resumeContent != null && !resumeContent.isBlank()) {
            questions.add("Your resume mentions relevant experience. Can you walk me through a project that best demonstrates your fit for this role?");
        }

        questions.addAll(BASE_QUESTIONS);

        return questions.stream().limit(5).toList();
    }

    @Override
    public FeedbackResult generateFeedback(String questionText, String answerText, String jobTitle) {
        int score = calculateScore(answerText);
        String feedback = buildFeedback(questionText, answerText, jobTitle, score);

        return FeedbackResult.builder()
                .feedbackText(feedback)
                .score(score)
                .build();
    }

    private int calculateScore(String answerText) {
        if (answerText == null || answerText.isBlank()) {
            return 0;
        }

        int length = answerText.trim().length();
        if (length < 20) {
            return 30;
        } else if (length < 100) {
            return 55;
        } else if (length < 300) {
            return 72;
        } else if (length < 800) {
            return 85;
        }
        return 92;
    }

    private String buildFeedback(String questionText, String answerText, String jobTitle, int score) {
        StringBuilder feedback = new StringBuilder();
        feedback.append("Mock AI Feedback for ").append(jobTitle).append(" interview:\n\n");

        if (answerText == null || answerText.isBlank()) {
            feedback.append("No answer was provided. Please submit a detailed response to receive meaningful feedback.");
            return feedback.toString();
        }

        feedback.append("Question: ").append(questionText).append("\n\n");

        if (score >= 80) {
            feedback.append("Strong answer. You provided a detailed, structured response with good depth. ");
            feedback.append("Consider adding a concrete metric or outcome to make it even stronger.");
        } else if (score >= 60) {
            feedback.append("Good start. Your answer covers the basics but could benefit from more specific examples ");
            feedback.append("and clearer structure using the STAR method (Situation, Task, Action, Result).");
        } else {
            feedback.append("Your answer needs more detail. Try to include a specific example, explain your approach, ");
            feedback.append("and describe the outcome or impact of your actions.");
        }

        feedback.append("\n\nScore: ").append(score).append("/100");
        return feedback.toString();
    }
}
