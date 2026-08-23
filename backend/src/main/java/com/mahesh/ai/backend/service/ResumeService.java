package com.mahesh.ai.backend.service;

import com.mahesh.ai.backend.dto.response.ResumeResponse;
import com.mahesh.ai.backend.entity.Resume;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ResumeService {
    ResumeResponse uploadResume(MultipartFile file, Long userId) throws IOException;
    ResumeResponse getResume(Long id, Long userId);
    Resume getResumeEntity(Long id, Long userId);
    List<ResumeResponse> getAllResumes(Long userId);
    void deleteResume(Long id, Long userId);
}
