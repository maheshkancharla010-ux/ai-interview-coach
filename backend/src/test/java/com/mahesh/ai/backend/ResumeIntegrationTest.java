package com.mahesh.ai.backend;

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
class ResumeIntegrationTest extends AbstractIntegrationTest {

    private String userAToken;
    private String userBToken;

    @BeforeEach
    void setUp() throws Exception {
        userAToken = registerAndGetToken("User A", "resume-a@example.com", "password123");
        userBToken = registerAndGetToken("User B", "resume-b@example.com", "password123");
    }

    @Test
    void uploadRetrieveListDelete() throws Exception {
        Long resumeId = uploadResume(userAToken, "resume.txt", "Java developer with 5 years experience");

        mockMvc.perform(get("/api/resumes/" + resumeId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.fileName").value("resume.txt"));

        mockMvc.perform(get("/api/resumes")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(1));

        mockMvc.perform(delete("/api/resumes/" + resumeId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/resumes/" + resumeId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void userACannotAccessUserBResume() throws Exception {
        Long resumeId = uploadResume(userBToken, "secret.txt", "Confidential resume content");

        mockMvc.perform(get("/api/resumes/" + resumeId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void userACannotDeleteUserBResume() throws Exception {
        Long resumeId = uploadResume(userBToken, "secret.txt", "Confidential resume content");

        mockMvc.perform(delete("/api/resumes/" + resumeId)
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isForbidden());
    }

    @Test
    void nonExistentResume() throws Exception {
        mockMvc.perform(get("/api/resumes/999999")
                        .header("Authorization", "Bearer " + userAToken))
                .andExpect(status().isNotFound());
    }
}
