package com.agn1kobi.e_commerce_backend.order.repository;

import com.agn1kobi.e_commerce_backend.order.model.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {}
