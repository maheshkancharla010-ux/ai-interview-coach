package com.mahesh.ai.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswerRequest {

    @NotBlank(message = "Answer text cannot be blank.")
    @Size(max = 5000, message = "Answer text must not exceed 5000 characters.")
    private String answerText;
}
