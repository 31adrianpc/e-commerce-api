package com.ecommerce.api.service.impl;

import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.api.dto.request.UserLoginRequestDTO;
import com.ecommerce.api.dto.request.UserRegisterRequestDTO;
import com.ecommerce.api.dto.request.UserUpdateProfileRequestDTO;
import com.ecommerce.api.dto.request.UserUpdateRequestDTO;
import com.ecommerce.api.dto.response.UserResponseDTO;
import com.ecommerce.api.entity.UserEntity;
import com.ecommerce.api.entity.UserEntity.USER_ROLE;
import com.ecommerce.api.exception.DuplicateResourceException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.mapper.UserMapper;
import com.ecommerce.api.repository.UserRepository;
import com.ecommerce.api.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void deleteUser(Long id) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                                        .orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con dicho ID"));
        return userMapper.toResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getUsersByRole(USER_ROLE role) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<UserResponseDTO> getUsersByStatus(boolean active) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    
    public String login(UserLoginRequestDTO request) {
        UserEntity user = findActiveUserByIdentifier(request);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Contraseña incorrecta");

        return "fake-jwt-token";
    }

    public UserEntity findActiveUserByIdentifier(UserLoginRequestDTO request) {
        UserEntity user;
        if (request.getIdentifier().contains("@")){
            user = userRepository.findByEmailAndActive(request.getIdentifier(), true)
                                 .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningun usuario activo con dicho email"));
        }
        else {
            user = userRepository.findByUsernameAndActive(request.getIdentifier(), true)
                                 .orElseThrow(() -> new ResourceNotFoundException("No se encontrón ningun usuario activo con dicho username"));
        }
        return user;
    }

    @Override
    public UserResponseDTO register(UserRegisterRequestDTO request) {

        if (userRepository.existsByUsername(request.getUsername()))
            throw new DuplicateResourceException("El username ya existe");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Email ya registrado");

        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));

        UserEntity userRegistered = userRepository.save(userEntity);

        return userMapper.toResponseDTO(userRegistered);
    }

    @Override
    public UserResponseDTO updateProfile(Long id, UserUpdateProfileRequestDTO request) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO request) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
