package com.mahesh.ai.backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class FeedbackIntegrationTest extends AbstractIntegrationTest {

    private String userAToken;
    private String userBToken;
    private Long userAInterviewId;
    private Long questionId;

    @BeforeEach
    void setUp() throws Exception {
        userAToken = registerAndGetToken("User A", "feedback-a@example.com", "password123");
        userBToken = registerAndGetToken("User B", "feedback-b@example.com", "password123");
        userAInterviewId = createInterview(userAToken, "Full Stack Developer");
        questionId = generateQuestions(userAToken, userAInterviewId);
        submitAnswer(userAToken, questionId,
                "I have extensive experience with React and Node.js, having built multiple production applications.");
    }

    @Test
    void generateAndRetrieveFeedbackForQuestion() throws Exception {
        mockMvc.perform(post("/api/questions/" + questionId + "/feedback/generate")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.feedbackText").isNotEmpty())
                .andExpect(jsonPath("$.data.score").isNumber())
                .andExpect(jsonPath("$.data.questionId").value(questionId));
    }

    @Test
    void generateFeedbackForInterview() throws Exception {
        mockMvc.perform(post("/api/interviews/" + userAInterviewId + "/feedback/generate")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].score").isNumber());
    }

    @Test
    void getFeedbackByInterview() throws Exception {
        mockMvc.perform(post("/api/interviews/" + userAInterviewId + "/feedback/generate")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/interviews/" + userAInterviewId + "/feedback")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(5));
    }

    @Test
    void crossUserAccessDenied() throws Exception {
        mockMvc.perform(get("/api/interviews/" + userAInterviewId + "/feedback")
                        .header("Authorization", "Bearer " + userBToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void mockAiFeedbackGeneratesScore() throws Exception {
        submitAnswer(userAToken, questionId,
                "In my previous role at TechCorp, I led a team of five developers building a microservices platform. " +
                "We used React for the frontend and Node.js for backend services, serving over 100,000 daily users. " +
                "I implemented CI/CD pipelines and reduced deployment time by 60 percent.");

        mockMvc.perform(post("/api/questions/" + questionId + "/feedback/generate")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.score").value(72));
    }
}
