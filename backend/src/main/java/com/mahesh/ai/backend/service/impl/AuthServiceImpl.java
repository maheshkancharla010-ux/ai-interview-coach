package com.mahesh.ai.backend.service.impl;

import com.mahesh.ai.backend.dto.request.LoginRequest;
import com.mahesh.ai.backend.dto.request.RegisterRequest;
import com.mahesh.ai.backend.dto.response.AuthResponse;
import com.mahesh.ai.backend.dto.response.UserResponse;
import com.mahesh.ai.backend.entity.User;
import com.mahesh.ai.backend.enums.Role;
import com.mahesh.ai.backend.exception.DuplicateResourceException;
import com.mahesh.ai.backend.exception.ResourceNotFoundException;
import com.mahesh.ai.backend.mapper.UserMapper;
import com.mahesh.ai.backend.repository.UserRepository;
import com.mahesh.ai.backend.security.JwtTokenProvider;
import com.mahesh.ai.backend.security.UserPrincipal;
import com.mahesh.ai.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);
        UserResponse userResponse = UserMapper.toResponse(savedUser);

        String token = jwtTokenProvider.generateToken(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getRole().name()
        );

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));

        UserResponse userResponse = UserMapper.toResponse(user);
        String token = jwtTokenProvider.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        return AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();
    }
}
