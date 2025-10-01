package com.ecommerce.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.api.entity.CartItemEntity;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    // Limpiar carrito
    void deleteAllByCartId(Long cartId);

    // Listar items
    List<CartItemEntity> findByCartId(Long cartId);

    // Validar duplicados
    Optional<CartItemEntity> findByCartIdAndProductId(Long cartId, Long productId);

    // Eliminar producto del carrito
    void deleteByCartIdAndProductId(Long cartId, Long productId);
}
