package com.mahesh.ai.backend.controller;

import com.mahesh.ai.backend.common.ApiResponse;
import com.mahesh.ai.backend.dto.request.QuestionAnswerRequest;
import com.mahesh.ai.backend.dto.response.QuestionResponse;
import com.mahesh.ai.backend.service.QuestionService;
import com.mahesh.ai.backend.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Questions", description = "Interview question management APIs")
public class QuestionController {

    private final QuestionService questionService;

    @PostMapping("/api/interviews/{interviewId}/questions/generate")
    @Operation(summary = "Generate interview questions using mock AI")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> generateQuestions(
            @PathVariable Long interviewId) {

        Long userId = SecurityUtil.getCurrentUserId();
        List<QuestionResponse> response = questionService.generateQuestions(interviewId, userId);

        ApiResponse<List<QuestionResponse>> apiResponse = ApiResponse.<List<QuestionResponse>>builder()
                .success(true)
                .message("Questions generated successfully.")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/api/interviews/{interviewId}/questions")
    @Operation(summary = "Get all questions for an interview")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuestionsByInterview(
            @PathVariable Long interviewId) {

        Long userId = SecurityUtil.getCurrentUserId();
        List<QuestionResponse> response = questionService.getQuestionsByInterview(interviewId, userId);

        ApiResponse<List<QuestionResponse>> apiResponse = ApiResponse.<List<QuestionResponse>>builder()
                .success(true)
                .message("Questions fetched successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/api/questions/{id}")
    @Operation(summary = "Get a question by ID")
    public ResponseEntity<ApiResponse<QuestionResponse>> getQuestion(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        QuestionResponse response = questionService.getQuestion(id, userId);

        ApiResponse<QuestionResponse> apiResponse = ApiResponse.<QuestionResponse>builder()
                .success(true)
                .message("Question fetched successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/api/questions/{id}/answer")
    @Operation(summary = "Submit an answer for a question")
    public ResponseEntity<ApiResponse<QuestionResponse>> submitAnswer(
            @PathVariable Long id,
            @Valid @RequestBody QuestionAnswerRequest request) {

        Long userId = SecurityUtil.getCurrentUserId();
        QuestionResponse response = questionService.submitAnswer(id, request, userId);

        ApiResponse<QuestionResponse> apiResponse = ApiResponse.<QuestionResponse>builder()
                .success(true)
                .message("Answer submitted successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
