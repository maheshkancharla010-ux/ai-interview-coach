package com.mahesh.ai.backend.service.impl;

import com.mahesh.ai.backend.dto.response.FeedbackResponse;
import com.mahesh.ai.backend.dto.response.FeedbackResult;
import com.mahesh.ai.backend.entity.Feedback;
import com.mahesh.ai.backend.entity.Interview;
import com.mahesh.ai.backend.entity.Question;
import com.mahesh.ai.backend.enums.InterviewStatus;
import com.mahesh.ai.backend.exception.ResourceNotFoundException;
import com.mahesh.ai.backend.mapper.FeedbackMapper;
import com.mahesh.ai.backend.repository.FeedbackRepository;
import com.mahesh.ai.backend.repository.QuestionRepository;
import com.mahesh.ai.backend.service.AiService;
import com.mahesh.ai.backend.service.FeedbackService;
import com.mahesh.ai.backend.service.InterviewService;
import com.mahesh.ai.backend.service.QuestionService;
import com.mahesh.ai.backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final QuestionRepository questionRepository;
    private final InterviewService interviewService;
    private final QuestionService questionService;
    private final AiService aiService;

    @Override
    @Transactional
    public FeedbackResponse generateFeedbackForQuestion(Long questionId, Long userId) {
        Question question = questionService.getQuestionEntity(questionId, userId);
        Interview interview = question.getInterview();

        FeedbackResult result = aiService.generateFeedback(
                question.getQuestionText(),
                question.getAnswerText(),
                interview.getJobTitle()
        );

        Feedback feedback = feedbackRepository.findByQuestionId(questionId)
                .orElse(Feedback.builder()
                        .interview(interview)
                        .question(question)
                        .build());

        feedback.setFeedbackText(result.getFeedbackText());
        feedback.setScore(result.getScore());

        Feedback savedFeedback = feedbackRepository.save(feedback);
        return FeedbackMapper.toResponse(savedFeedback);
    }

    @Override
    @Transactional
    public List<FeedbackResponse> generateFeedbackForInterview(Long interviewId, Long userId) {
        Interview interview = interviewService.getInterviewEntity(interviewId, userId);
        List<Question> questions = questionRepository.findByInterviewId(interviewId);

        if (questions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for interview ID " + interviewId);
        }

        List<FeedbackResponse> feedbackList = new ArrayList<>();
        for (Question question : questions) {
            FeedbackResult result = aiService.generateFeedback(
                    question.getQuestionText(),
                    question.getAnswerText(),
                    interview.getJobTitle()
            );

            Feedback feedback = feedbackRepository.findByQuestionId(question.getId())
                    .orElse(Feedback.builder()
                            .interview(interview)
                            .question(question)
                            .build());

            feedback.setFeedbackText(result.getFeedbackText());
            feedback.setScore(result.getScore());

            feedbackList.add(FeedbackMapper.toResponse(feedbackRepository.save(feedback)));
        }

        interview.setStatus(InterviewStatus.COMPLETED);
        return feedbackList;
    }

    @Override
    public List<FeedbackResponse> getFeedbackByInterview(Long interviewId, Long userId) {
        interviewService.getInterviewEntity(interviewId, userId);

        return feedbackRepository.findByInterviewId(interviewId).stream()
                .map(FeedbackMapper::toResponse)
                .toList();
    }

    @Override
    public FeedbackResponse getFeedback(Long id, Long userId) {
        Feedback feedback = getFeedbackEntity(id, userId);
        return FeedbackMapper.toResponse(feedback);
    }

    @Override
    public Feedback getFeedbackEntity(Long id, Long userId) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Feedback not found with ID " + id));

        checkOwnership(feedback, userId);
        return feedback;
    }

    private void checkOwnership(Feedback feedback, Long userId) {
        if (!feedback.getInterview().getUser().getId().equals(userId) && !SecurityUtil.isCurrentUserAdmin()) {
            throw new AccessDeniedException("Access denied. You do not own this feedback.");
        }
    }
}
