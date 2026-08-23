package com.mahesh.ai.backend.dto.response;

import com.mahesh.ai.backend.enums.InterviewStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InterviewResponse {
    private Long id;
    private String jobTitle;
    private String jobDescription;
    private InterviewStatus status;
    private Long resumeId;
    private Long userId;
    private LocalDateTime createdAt;
}
