package com.ecommerce.api.service;

import java.util.List;

import com.ecommerce.api.dto.request.*;
import com.ecommerce.api.dto.response.UserResponseDTO;
import com.ecommerce.api.entity.UserEntity.USER_ROLE;

public interface UserService {

    UserResponseDTO register(UserRegisterRequestDTO request);
    UserResponseDTO createUser(UserCreateRequestDTO request);

    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO request);
    UserResponseDTO updateProfile(Long id, UserUpdateProfileRequestDTO request);
    void deleteUser(Long id);

    List<UserResponseDTO> getAllUsers();
    List<UserResponseDTO> getUsersByRole(USER_ROLE role);
    List<UserResponseDTO> getUsersByStatus(boolean active);

}
