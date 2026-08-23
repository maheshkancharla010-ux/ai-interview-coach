package com.mahesh.ai.backend.service.impl;
import java.util.List;
import java.util.Optional;

import com.mahesh.ai.backend.dto.request.UserRequest;
import com.mahesh.ai.backend.dto.response.UserResponse;
import com.mahesh.ai.backend.exception.DuplicateResourceException;
import com.mahesh.ai.backend.repository.UserRepository;
import com.mahesh.ai.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.mahesh.ai.backend.exception.ResourceNotFoundException;
import com.mahesh.ai.backend.entity.User;
import com.mahesh.ai.backend.mapper.UserMapper;
import com.mahesh.ai.backend.enums.Role;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with ID " + id + " not found."
                        ));

        userRepository.delete(user);
    }
    @Override
    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with ID " + id + " not found."
                        ));

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent() &&
                !existingUser.get().getId().equals(id)) {

            throw new DuplicateResourceException("Email already exists.");
        }

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        User updatedUser = userRepository.save(user);

        return UserMapper.toResponse(updatedUser);
    }
    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        User user = UserMapper.toEntity(request);
        user.setRole(Role.USER);

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User with ID " + id + " not found."
                        ));

        return UserMapper.toResponse(user);
    }
}
