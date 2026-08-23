package com.mahesh.ai.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ResumeResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private String content;
    private Long userId;
    private LocalDateTime createdAt;
}
