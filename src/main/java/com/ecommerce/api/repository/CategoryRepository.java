package com.ecommerce.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.api.entity.CategoryEntity;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    // Para validar y no crear una categoría existente
    boolean existsByName(String name);

    // Búsquedas
    Optional<CategoryEntity> findByName(String name);

    // Gestión de categorías
    List<CategoryEntity> findByIsActive(boolean isActive);
    
    // Para mostrar a los usuarios
    List<CategoryEntity> findByIsActiveTrue();
}