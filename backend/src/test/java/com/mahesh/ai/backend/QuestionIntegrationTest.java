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
class QuestionIntegrationTest extends AbstractIntegrationTest {

    private String userAToken;
    private String userBToken;
    private Long userAInterviewId;
    private Long userBInterviewId;

    @BeforeEach
    void setUp() throws Exception {
        userAToken = registerAndGetToken("User A", "question-a@example.com", "password123");
        userBToken = registerAndGetToken("User B", "question-b@example.com", "password123");
        userAInterviewId = createInterview(userAToken, "Java Developer");
        userBInterviewId = createInterview(userBToken, "Python Developer");
    }

    @Test
    void generateRetrieveSubmitAnswer() throws Exception {
        Long questionId = generateQuestions(userAToken, userAInterviewId);

        mockMvc.perform(get("/api/interviews/" + userAInterviewId + "/questions")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(5));

        mockMvc.perform(get("/api/questions/" + questionId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.questionText").isNotEmpty());

        submitAnswer(userAToken, questionId,
                "I have 5 years of experience building scalable Java applications with Spring Boot.");

        mockMvc.perform(get("/api/questions/" + questionId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.answerText").isNotEmpty());
    }

    @Test
    void crossUserAccessDenied() throws Exception {
        Long questionId = generateQuestions(userBToken, userBInterviewId);

        mockMvc.perform(get("/api/questions/" + questionId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void invalidInterview() throws Exception {
        mockMvc.perform(post("/api/interviews/999999/questions/generate")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void mockGenerationProducesQuestions() throws Exception {
        mockMvc.perform(post("/api/interviews/" + userAInterviewId + "/questions/generate")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.length()").value(5))
                .andExpect(jsonPath("$.data[0].questionText").isNotEmpty());
    }
}
