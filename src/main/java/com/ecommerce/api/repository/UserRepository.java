package com.ecommerce.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.api.entity.UserEntity;
import com.ecommerce.api.entity.UserEntity.USER_ROLE;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    // Para el registro
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    // Para el login
    Optional<UserEntity> findByUsernameAndIsActive(String username, boolean isActive);
    Optional<UserEntity> findByEmailAndIsActive(String email,  boolean isActive);

    // Gestión de usuarios
    List<UserEntity> findByRoleAndIsActive(USER_ROLE role, boolean isActive);
    List<UserEntity> findByIsActive(boolean isActive);
}