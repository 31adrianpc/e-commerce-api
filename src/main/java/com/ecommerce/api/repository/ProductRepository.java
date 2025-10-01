package com.ecommerce.api.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.api.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    //Validar el registro
    boolean existsByName(String name);

    //Búsquedas admin
    Optional<ProductEntity> findByName(String name);

    //Gestión
    List<ProductEntity> findByIsActive(boolean isActive);

    //Para usuarios
    List<ProductEntity> findByIsActiveTrue(); // productos activos
    List<ProductEntity> findByCategoryIdAndIsActiveTrue(Long categoryId); // productos activo de una categoria
    List<ProductEntity> findByPriceBetweenAndIsActiveTrue(BigDecimal minPrice, BigDecimal maxPrice); // productos por precio
    List<ProductEntity> findByNameContainingIgnoreCaseAndIsActiveTrue(String searchTerm); // productos por nombre
    List<ProductEntity> findByStockGreaterThanAndIsActiveTrue(Integer minStock); // productos por stock
}
