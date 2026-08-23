package com.mahesh.ai.backend.controller;

import com.mahesh.ai.backend.common.ApiResponse;
import com.mahesh.ai.backend.dto.response.FeedbackResponse;
import com.mahesh.ai.backend.service.FeedbackService;
import com.mahesh.ai.backend.util.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Feedback", description = "Interview feedback management APIs")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/api/questions/{questionId}/feedback/generate")
    @Operation(summary = "Generate feedback for a specific question answer")
    public ResponseEntity<ApiResponse<FeedbackResponse>> generateFeedbackForQuestion(
            @PathVariable Long questionId) {

        Long userId = SecurityUtil.getCurrentUserId();
        FeedbackResponse response = feedbackService.generateFeedbackForQuestion(questionId, userId);

        ApiResponse<FeedbackResponse> apiResponse = ApiResponse.<FeedbackResponse>builder()
                .success(true)
                .message("Feedback generated successfully.")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PostMapping("/api/interviews/{interviewId}/feedback/generate")
    @Operation(summary = "Generate feedback for all questions in an interview")
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> generateFeedbackForInterview(
            @PathVariable Long interviewId) {

        Long userId = SecurityUtil.getCurrentUserId();
        List<FeedbackResponse> response = feedbackService.generateFeedbackForInterview(interviewId, userId);

        ApiResponse<List<FeedbackResponse>> apiResponse = ApiResponse.<List<FeedbackResponse>>builder()
                .success(true)
                .message("Feedback generated successfully for all questions.")
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @GetMapping("/api/interviews/{interviewId}/feedback")
    @Operation(summary = "Get all feedback for an interview")
    public ResponseEntity<ApiResponse<List<FeedbackResponse>>> getFeedbackByInterview(
            @PathVariable Long interviewId) {

        Long userId = SecurityUtil.getCurrentUserId();
        List<FeedbackResponse> response = feedbackService.getFeedbackByInterview(interviewId, userId);

        ApiResponse<List<FeedbackResponse>> apiResponse = ApiResponse.<List<FeedbackResponse>>builder()
                .success(true)
                .message("Feedback fetched successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/api/feedback/{id}")
    @Operation(summary = "Get feedback by ID")
    public ResponseEntity<ApiResponse<FeedbackResponse>> getFeedback(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        FeedbackResponse response = feedbackService.getFeedback(id, userId);

        ApiResponse<FeedbackResponse> apiResponse = ApiResponse.<FeedbackResponse>builder()
                .success(true)
                .message("Feedback fetched successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
