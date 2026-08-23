package com.mahesh.ai.backend.service;

import com.mahesh.ai.backend.dto.request.InterviewRequest;
import com.mahesh.ai.backend.dto.response.InterviewResponse;
import com.mahesh.ai.backend.entity.Interview;
import com.mahesh.ai.backend.enums.InterviewStatus;

import java.util.List;

public interface InterviewService {
    InterviewResponse createInterview(InterviewRequest request, Long userId);
    InterviewResponse getInterview(Long id, Long userId);
    Interview getInterviewEntity(Long id, Long userId);
    List<InterviewResponse> getAllInterviews(Long userId);
    void deleteInterview(Long id, Long userId);
    InterviewResponse updateInterviewStatus(Long id, InterviewStatus status, Long userId);
}
