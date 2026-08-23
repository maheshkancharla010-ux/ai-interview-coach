package com.mahesh.ai.backend.mapper;

import com.mahesh.ai.backend.dto.response.ResumeResponse;
import com.mahesh.ai.backend.entity.Resume;

public class ResumeMapper {

    public static ResumeResponse toResponse(Resume resume) {
        if (resume == null) {
            return null;
        }

        return ResumeResponse.builder()
                .id(resume.getId())
                .fileName(resume.getFileName())
                .fileType(resume.getFileType())
                .content(resume.getContent())
                .userId(resume.getUser() != null ? resume.getUser().getId() : null)
                .createdAt(resume.getCreatedAt())
                .build();
    }
}
