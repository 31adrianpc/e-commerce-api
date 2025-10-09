package com.ecommerce.api.service;

import java.util.List;

import com.ecommerce.api.dto.request.UserLoginRequestDTO;
import com.ecommerce.api.dto.request.UserRegisterRequestDTO;
import com.ecommerce.api.dto.request.UserUpdateProfileRequestDTO;
import com.ecommerce.api.dto.request.UserUpdateRequestDTO;
import com.ecommerce.api.dto.response.UserResponseDTO;
import com.ecommerce.api.entity.UserEntity.USER_ROLE;

public interface UserService {

    // Autenticacion
    UserResponseDTO register(UserRegisterRequestDTO request);
    String login(UserLoginRequestDTO request); // retorna un JWT
    
    UserResponseDTO getUserById(Long id);
    UserResponseDTO updateUser(Long id, UserUpdateRequestDTO request);
    UserResponseDTO updateProfile(Long id, UserUpdateProfileRequestDTO request);
    void deleteUser(Long id);

    List<UserResponseDTO> getAllUsers();
    List<UserResponseDTO> getUsersByRole(USER_ROLE role);
    List<UserResponseDTO> getUsersByStatus(boolean active);

}
