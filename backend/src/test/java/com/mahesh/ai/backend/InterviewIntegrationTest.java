package com.mahesh.ai.backend;

import com.mahesh.ai.backend.dto.request.InterviewRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class InterviewIntegrationTest extends AbstractIntegrationTest {

    private String userAToken;
    private String userBToken;

    @BeforeEach
    void setUp() throws Exception {
        userAToken = registerAndGetToken("User A", "interview-a@example.com", "password123");
        userBToken = registerAndGetToken("User B", "interview-b@example.com", "password123");
    }

    @Test
    void createRetrieveDelete() throws Exception {
        Long interviewId = createInterview(userAToken, "Senior Java Developer");

        mockMvc.perform(get("/api/interviews/" + interviewId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.jobTitle").value("Senior Java Developer"));

        mockMvc.perform(delete("/api/interviews/" + interviewId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/interviews/" + interviewId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void cannotUseAnotherUsersResume() throws Exception {
        Long resumeId = uploadResume(userBToken, "resume.txt", "User B resume");

        InterviewRequest request = new InterviewRequest();
        request.setJobTitle("Backend Developer");
        request.setJobDescription("Java role");
        request.setResumeId(resumeId);

        mockMvc.perform(post("/api/interviews")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    void crossUserInterviewAccess() throws Exception {
        Long interviewId = createInterview(userBToken, "Private Interview");

        mockMvc.perform(get("/api/interviews/" + interviewId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void invalidResumeId() throws Exception {
        InterviewRequest request = new InterviewRequest();
        request.setJobTitle("Backend Developer");
        request.setResumeId(999999L);

        mockMvc.perform(post("/api/interviews")
                        .header("Authorization", "Bearer " + userAToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStatus() throws Exception {
        Long interviewId = createInterview(userAToken, "Status Test Role");

        mockMvc.perform(put("/api/interviews/" + interviewId + "/status")
                        .param("status", "IN_PROGRESS")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("IN_PROGRESS"));
    }
}
