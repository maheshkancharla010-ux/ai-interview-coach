package com.mahesh.ai.backend.mapper;

import com.mahesh.ai.backend.dto.response.InterviewResponse;
import com.mahesh.ai.backend.entity.Interview;

public class InterviewMapper {

    public static InterviewResponse toResponse(Interview interview) {
        if (interview == null) {
            return null;
        }

        return InterviewResponse.builder()
                .id(interview.getId())
                .jobTitle(interview.getJobTitle())
                .jobDescription(interview.getJobDescription())
                .status(interview.getStatus())
                .resumeId(interview.getResume() != null ? interview.getResume().getId() : null)
                .userId(interview.getUser() != null ? interview.getUser().getId() : null)
                .createdAt(interview.getCreatedAt())
                .build();
    }
}
