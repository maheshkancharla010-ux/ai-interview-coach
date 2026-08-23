package com.mahesh.ai.backend.dto.response;

import com.mahesh.ai.backend.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponse {
    private Role role;
    private Long id;

    private String fullName;

    private String email;

    private Boolean active;

    private LocalDateTime createdAt;

}