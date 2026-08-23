package com.mahesh.ai.backend.service;
import java.util.List;
import com.mahesh.ai.backend.dto.request.UserRequest;
import com.mahesh.ai.backend.dto.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest request);
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserRequest request);
    void deleteUser(Long id);
}