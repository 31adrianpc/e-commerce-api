package com.ecommerce.api.service.impl;

import java.util.List;

import com.ecommerce.api.dto.request.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.api.dto.response.UserResponseDTO;
import com.ecommerce.api.entity.UserEntity;
import com.ecommerce.api.entity.UserEntity.USER_ROLE;
import com.ecommerce.api.exception.DuplicateResourceException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.mapper.UserMapper;
import com.ecommerce.api.repository.UserRepository;
import com.ecommerce.api.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Transactional // mantener sincronización entre BD y contexto de persistencia
    @Override
    public void deleteUser(Long id) {
        UserEntity user = findUserById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository
            .findAll()
            .stream()
            .map(userMapper::toResponseDTO)
            .toList();
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        UserEntity user = findUserById(id);
        return userMapper.toResponseDTO(user);
    }

    @Override
    public List<UserResponseDTO> getUsersByRole(USER_ROLE role) {
        return userRepository
            .findByRoleAndActive(role, true)
            .stream()
            .map(userMapper::toResponseDTO)
            .toList();
    }

    @Override
    public List<UserResponseDTO> getUsersByStatus(boolean active) {
        return userRepository
            .findByActive(active)
            .stream()
            .map(userMapper::toResponseDTO)
            .toList();
    }

    @Override
    public String login(UserLoginRequestDTO request) {
        UserEntity user = findActiveUserByIdentifier(request);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new BadCredentialsException("Contraseña incorrecta");

        return "fake-jwt-token";
    }

    @Transactional
    @Override
    public UserResponseDTO register(UserRegisterRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new DuplicateResourceException("El username ya existe");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Email ya registrado");

        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setActive(true);
        userEntity.setRole(USER_ROLE.CUSTOMER);

        return userMapper.toResponseDTO(userRepository.save(userEntity));
    }

    @Transactional // evitar problemas de concurrencia
    @Override
    public UserResponseDTO createUser(UserCreateRequestDTO request) {
        if (userRepository.existsByUsername(request.getUsername()))
            throw new DuplicateResourceException("El username ya existe");

        if (userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Email ya registrado");

        UserEntity userEntity = userMapper.toEntity(request);
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setActive(true);
        userEntity.setRole(request.getRole());

        return userMapper.toResponseDTO(userRepository.save(userEntity));
    }

    @Transactional
    @Override
    public UserResponseDTO updateProfile(Long id, UserUpdateProfileRequestDTO request) {
        UserEntity user = findUserById(id);

        userMapper.updateProfileFromDTO(request, user);

        if (request.getPassword()!=null && !request.getPassword().isBlank())
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        UserEntity userSaved = userRepository.save(user);
        return userMapper.toResponseDTO(userSaved);
    }

    @Transactional
    @Override
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO request) {
        UserEntity user = findUserById(id);

        if (request.getUsername()!=null &&
            !request.getUsername().equals(user.getUsername()) &&
            userRepository.existsByUsername(request.getUsername()))
            throw new DuplicateResourceException("Ya existe un usuario con dicho username");
        
        if (request.getEmail()!=null &&
            !request.getEmail().equals(user.getEmail()) &&
            userRepository.existsByEmail(request.getEmail()))
            throw new DuplicateResourceException("Ya existe un usuario con dicho email");
        
        userMapper.updateEntityFromDTO(request,user);
        UserEntity userSaved = userRepository.save(user);
        return userMapper.toResponseDTO(userSaved);
    }

    /* METODOS PRIVADOS HELPERS */
    private UserEntity findActiveUserByIdentifier(UserLoginRequestDTO request) {
        if (request.getIdentifier().contains("@")){
            return userRepository.findByEmailAndActive(request.getIdentifier(), true)
                                 .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningun usuario activo con dicho email"));
        }
        else {
            return userRepository.findByUsernameAndActive(request.getIdentifier(), true)
                                 .orElseThrow(() -> new ResourceNotFoundException("No se encontró ningun usuario activo con dicho username"));
        }
    }
    
    private UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                             .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }
}
