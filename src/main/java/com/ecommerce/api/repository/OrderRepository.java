package com.ecommerce.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.api.entity.OrderEntity;
import com.ecommerce.api.entity.OrderEntity.ORDER_STATUS;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    
    // Gestion admin
    List<OrderEntity> findByStatus(ORDER_STATUS status);
    List<OrderEntity> findByStatusIn(List<ORDER_STATUS> statuses);

    // Buscar por usuario
    List<OrderEntity> findByUserId(Long userId);
    List<OrderEntity> findByUserIdAndStatus(Long userId, ORDER_STATUS status);

    // Buscar por número de orden
    Optional<OrderEntity> findByOrderNumber(String orderNumber);
}