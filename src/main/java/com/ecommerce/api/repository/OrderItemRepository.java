package com.ecommerce.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.api.entity.OrderItemEntity;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    // Items de una orden
    List<OrderItemEntity> findByOrderId(Long orderId);
    
    // Ventas de un producto
    List<OrderItemEntity> findByProductId(Long productId);
}
