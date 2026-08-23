package com.mahesh.ai.backend.mapper;

import com.mahesh.ai.backend.dto.request.UserRequest;
import com.mahesh.ai.backend.dto.response.UserResponse;
import com.mahesh.ai.backend.entity.User;

public class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserRequest request) {

        return User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(request.getPassword())
                .active(true)
                .build();
    }

    public static UserResponse toResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }
}