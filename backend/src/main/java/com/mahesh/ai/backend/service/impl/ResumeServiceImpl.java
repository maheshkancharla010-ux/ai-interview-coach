package com.mahesh.ai.backend.service.impl;

import com.mahesh.ai.backend.dto.response.ResumeResponse;
import com.mahesh.ai.backend.entity.Resume;
import com.mahesh.ai.backend.entity.User;
import com.mahesh.ai.backend.exception.ResourceNotFoundException;
import com.mahesh.ai.backend.mapper.ResumeMapper;
import com.mahesh.ai.backend.repository.ResumeRepository;
import com.mahesh.ai.backend.repository.UserRepository;
import com.mahesh.ai.backend.service.ResumeService;
import com.mahesh.ai.backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    @Override
    public ResumeResponse uploadResume(MultipartFile file, Long userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID " + userId));

        String content;
        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        if (contentType != null && contentType.contains("pdf") || (originalFilename != null && originalFilename.toLowerCase().endsWith(".pdf"))) {
            // Simulate PDF parsing
            content = "Extracted Resume Information:\n" +
                    "Name: " + user.getFullName() + "\n" +
                    "Email: " + user.getEmail() + "\n" +
                    "Source File: " + originalFilename + "\n" +
                    "Position Sought: Senior Software Engineer\n" +
                    "Core Skills: Java 21, Spring Boot, Spring Security, Hibernate, PostgreSQL, Docker, Microservices\n" +
                    "Experience: \n" +
                    "- TechCorp: Senior Backend Engineer (3 years). Managed Spring Boot databases.\n" +
                    "- DevSolutions: Java Developer (2 years). Built REST APIs and worked with SQL Databases.\n" +
                    "Education: B.S. in Computer Science";
        } else {
            byte[] bytes = file.getBytes();
            content = new String(bytes, StandardCharsets.UTF_8);
        }

        Resume resume = Resume.builder()
                .fileName(originalFilename != null ? originalFilename : "resume.txt")
                .fileType(contentType)
                .fileData(file.getBytes())
                .content(content)
                .user(user)
                .build();

        Resume savedResume = resumeRepository.save(resume);
        return ResumeMapper.toResponse(savedResume);
    }

    @Override
    public ResumeResponse getResume(Long id, Long userId) {
        Resume resume = getResumeEntity(id, userId);
        return ResumeMapper.toResponse(resume);
    }

    @Override
    public Resume getResumeEntity(Long id, Long userId) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID " + id));

        checkOwnership(resume, userId);
        return resume;
    }

    @Override
    public List<ResumeResponse> getAllResumes(Long userId) {
        // If current user is ADMIN, they can fetch all resumes. If not, they can only fetch their own.
        List<Resume> resumes;
        if (SecurityUtil.isCurrentUserAdmin()) {
            resumes = resumeRepository.findAll();
        } else {
            resumes = resumeRepository.findByUserId(userId);
        }
        return resumes.stream()
                .map(ResumeMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteResume(Long id, Long userId) {
        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID " + id));

        checkOwnership(resume, userId);
        resumeRepository.delete(resume);
    }

    private void checkOwnership(Resume resume, Long userId) {
        if (!resume.getUser().getId().equals(userId) && !SecurityUtil.isCurrentUserAdmin()) {
            throw new AccessDeniedException("Access denied. You do not own this resume.");
        }
    }
}
