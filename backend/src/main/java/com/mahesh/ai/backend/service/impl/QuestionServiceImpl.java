package com.mahesh.ai.backend.service.impl;

import com.mahesh.ai.backend.dto.request.QuestionAnswerRequest;
import com.mahesh.ai.backend.dto.response.QuestionResponse;
import com.mahesh.ai.backend.entity.Interview;
import com.mahesh.ai.backend.entity.Question;
import com.mahesh.ai.backend.enums.InterviewStatus;
import com.mahesh.ai.backend.exception.ResourceNotFoundException;
import com.mahesh.ai.backend.mapper.QuestionMapper;
import com.mahesh.ai.backend.repository.QuestionRepository;
import com.mahesh.ai.backend.service.AiService;
import com.mahesh.ai.backend.service.InterviewService;
import com.mahesh.ai.backend.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final InterviewService interviewService;
    private final AiService aiService;

    @Override
    @Transactional
    public List<QuestionResponse> generateQuestions(Long interviewId, Long userId) {
        Interview interview = interviewService.getInterviewEntity(interviewId, userId);

        String resumeContent = interview.getResume() != null ? interview.getResume().getContent() : null;

        List<String> generatedQuestions = aiService.generateQuestions(
                interview.getJobTitle(),
                interview.getJobDescription(),
                resumeContent
        );

        List<Question> savedQuestions = new ArrayList<>();
        for (String questionText : generatedQuestions) {
            Question question = Question.builder()
                    .questionText(questionText)
                    .interview(interview)
                    .build();
            savedQuestions.add(questionRepository.save(question));
        }

        if (interview.getStatus() == InterviewStatus.PENDING) {
            interview.setStatus(InterviewStatus.IN_PROGRESS);
        }

        return savedQuestions.stream()
                .map(QuestionMapper::toResponse)
                .toList();
    }

    @Override
    public List<QuestionResponse> getQuestionsByInterview(Long interviewId, Long userId) {
        interviewService.getInterviewEntity(interviewId, userId);

        return questionRepository.findByInterviewId(interviewId).stream()
                .map(QuestionMapper::toResponse)
                .toList();
    }

    @Override
    public QuestionResponse getQuestion(Long id, Long userId) {
        Question question = getQuestionEntity(id, userId);
        return QuestionMapper.toResponse(question);
    }

    @Override
    @Transactional
    public QuestionResponse submitAnswer(Long id, QuestionAnswerRequest request, Long userId) {
        Question question = getQuestionEntity(id, userId);
        question.setAnswerText(request.getAnswerText());
        Question savedQuestion = questionRepository.save(question);
        return QuestionMapper.toResponse(savedQuestion);
    }

    @Override
    public Question getQuestionEntity(Long id, Long userId) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found with ID " + id));

        interviewService.getInterviewEntity(question.getInterview().getId(), userId);
        return question;
    }
}
