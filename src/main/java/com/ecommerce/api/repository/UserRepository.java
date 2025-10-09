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
    Optional<UserEntity> findByUsernameAndActive(String username, boolean active);
    Optional<UserEntity> findByEmailAndActive(String email,  boolean active);

    // Gestión de usuarios
    List<UserEntity> findByRoleAndActive(USER_ROLE role, boolean active);
    List<UserEntity> findByActive(boolean active);
}