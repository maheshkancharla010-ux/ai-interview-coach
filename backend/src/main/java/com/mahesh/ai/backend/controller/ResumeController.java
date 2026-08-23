package com.mahesh.ai.backend.controller;

import com.mahesh.ai.backend.common.ApiResponse;
import com.mahesh.ai.backend.dto.response.ResumeResponse;
import com.mahesh.ai.backend.entity.Resume;
import com.mahesh.ai.backend.service.ResumeService;
import com.mahesh.ai.backend.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ResumeResponse>> uploadResume(
            @RequestParam("file") MultipartFile file) throws IOException {

        Long userId = SecurityUtil.getCurrentUserId();
        ResumeResponse response = resumeService.uploadResume(file, userId);

        ApiResponse<ResumeResponse> apiResponse = ApiResponse.<ResumeResponse>builder()
                .success(true)
                .message("Resume uploaded successfully.")
                .data(response)
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(apiResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> getAllResumes() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<ResumeResponse> response = resumeService.getAllResumes(userId);

        ApiResponse<List<ResumeResponse>> apiResponse = ApiResponse.<List<ResumeResponse>>builder()
                .success(true)
                .message("Resumes fetched successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> getResumeById(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        ResumeResponse response = resumeService.getResume(id, userId);

        ApiResponse<ResumeResponse> apiResponse = ApiResponse.<ResumeResponse>builder()
                .success(true)
                .message("Resume fetched successfully.")
                .data(response)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        resumeService.deleteResume(id, userId);

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .success(true)
                .message("Resume deleted successfully.")
                .data(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadResume(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        Resume resume = resumeService.getResumeEntity(id, userId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(resume.getFileType() != null ? resume.getFileType() : "application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resume.getFileName() + "\"")
                .body(resume.getFileData());
    }
}
