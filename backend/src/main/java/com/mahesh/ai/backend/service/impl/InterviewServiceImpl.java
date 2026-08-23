package com.mahesh.ai.backend.service.impl;

import com.mahesh.ai.backend.dto.request.InterviewRequest;
import com.mahesh.ai.backend.dto.response.InterviewResponse;
import com.mahesh.ai.backend.entity.Interview;
import com.mahesh.ai.backend.entity.Resume;
import com.mahesh.ai.backend.entity.User;
import com.mahesh.ai.backend.enums.InterviewStatus;
import com.mahesh.ai.backend.exception.ResourceNotFoundException;
import com.mahesh.ai.backend.mapper.InterviewMapper;
import com.mahesh.ai.backend.repository.InterviewRepository;
import com.mahesh.ai.backend.repository.ResumeRepository;
import com.mahesh.ai.backend.repository.UserRepository;
import com.mahesh.ai.backend.service.InterviewService;
import com.mahesh.ai.backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewRepository interviewRepository;
    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;

    @Override
    public InterviewResponse createInterview(InterviewRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID " + userId));

        Resume resume = null;
        if (request.getResumeId() != null) {
            resume = resumeRepository.findById(request.getResumeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Resume not found with ID " + request.getResumeId()));

            if (!resume.getUser().getId().equals(userId) && !SecurityUtil.isCurrentUserAdmin()) {
                throw new AccessDeniedException("Access denied. You do not own this resume.");
            }
        }

        Interview interview = Interview.builder()
                .jobTitle(request.getJobTitle())
                .jobDescription(request.getJobDescription())
                .status(InterviewStatus.PENDING)
                .user(user)
                .resume(resume)
                .build();

        Interview savedInterview = interviewRepository.save(interview);
        return InterviewMapper.toResponse(savedInterview);
    }

    @Override
    public InterviewResponse getInterview(Long id, Long userId) {
        Interview interview = getInterviewEntity(id, userId);
        return InterviewMapper.toResponse(interview);
    }

    @Override
    public Interview getInterviewEntity(Long id, Long userId) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with ID " + id));

        checkOwnership(interview, userId);
        return interview;
    }

    @Override
    public List<InterviewResponse> getAllInterviews(Long userId) {
        List<Interview> interviews;
        if (SecurityUtil.isCurrentUserAdmin()) {
            interviews = interviewRepository.findAll();
        } else {
            interviews = interviewRepository.findByUserId(userId);
        }
        return interviews.stream()
                .map(InterviewMapper::toResponse)
                .toList();
    }

    @Override
    public void deleteInterview(Long id, Long userId) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with ID " + id));

        checkOwnership(interview, userId);
        interviewRepository.delete(interview);
    }

    @Override
    public InterviewResponse updateInterviewStatus(Long id, InterviewStatus status, Long userId) {
        Interview interview = interviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Interview not found with ID " + id));

        checkOwnership(interview, userId);
        interview.setStatus(status);
        Interview savedInterview = interviewRepository.save(interview);
        return InterviewMapper.toResponse(savedInterview);
    }

    private void checkOwnership(Interview interview, Long userId) {
        if (!interview.getUser().getId().equals(userId) && !SecurityUtil.isCurrentUserAdmin()) {
            throw new AccessDeniedException("Access denied. You do not own this interview.");
        }
    }
}
