package com.mahesh.ai.backend.service;

import com.mahesh.ai.backend.dto.request.LoginRequest;
import com.mahesh.ai.backend.dto.request.RegisterRequest;
import com.mahesh.ai.backend.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
