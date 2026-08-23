package com.mahesh.ai.backend.controller;

import com.mahesh.ai.backend.common.ApiResponse;
import com.mahesh.ai.backend.dto.request.InterviewRequest;
import com.mahesh.ai.backend.dto.response.InterviewResponse;
import com.mahesh.ai.backend.enums.InterviewStatus;
import com.mahesh.ai.backend.service.InterviewService;
import com.mahesh.ai.backend.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService interviewService;

    @PostMapping
    public ResponseEntity<ApiResponse<InterviewResponse>> createInterview(
            @Valid @RequestBody InterviewRequest request) {

        Long userId = SecurityUtil.getCurrentUserId();
        InterviewResponse response = interviewService.createInterview(request, userId);

        ApiResponse<InterviewResponse> apiResponse = ApiResponse.<InterviewResponse>builder()
                .success(true)
                .message("Interview created successfully.")
                .data(response)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InterviewResponse>>> getAllInterviews() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<InterviewResponse> response = interviewService.getAllInterviews(userId);

        ApiResponse<List<InterviewResponse>> apiResponse = ApiResponse.<List<InterviewResponse>>builder()
                .success(true)
                .message("Interviews fetched successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InterviewResponse>> getInterviewById(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        InterviewResponse response = interviewService.getInterview(id, userId);

        ApiResponse<InterviewResponse> apiResponse = ApiResponse.<InterviewResponse>builder()
                .success(true)
                .message("Interview fetched successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteInterview(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        interviewService.deleteInterview(id, userId);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message("Interview deleted successfully.")
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<InterviewResponse>> updateStatus(
            @PathVariable Long id,
            @RequestParam("status") InterviewStatus status) {

        Long userId = SecurityUtil.getCurrentUserId();
        InterviewResponse response = interviewService.updateInterviewStatus(id, status, userId);

        ApiResponse<InterviewResponse> apiResponse = ApiResponse.<InterviewResponse>builder()
                .success(true)
                .message("Interview status updated successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}
