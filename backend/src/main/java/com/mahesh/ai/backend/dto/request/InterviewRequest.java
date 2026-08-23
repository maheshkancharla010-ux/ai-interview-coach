package com.mahesh.ai.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InterviewRequest {

    @NotBlank(message = "Job title is required.")
    @Size(min = 3, max = 150, message = "Job title must be between 3 and 150 characters.")
    private String jobTitle;

    private String jobDescription;

    private Long resumeId;
}
