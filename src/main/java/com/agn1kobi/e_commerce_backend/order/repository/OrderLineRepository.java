package com.agn1kobi.e_commerce_backend.order.repository;

import com.agn1kobi.e_commerce_backend.order.model.OrderLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderLineRepository extends JpaRepository<OrderLineEntity, UUID> {
    List<OrderLineEntity> findByOrderId(UUID orderId);
}
