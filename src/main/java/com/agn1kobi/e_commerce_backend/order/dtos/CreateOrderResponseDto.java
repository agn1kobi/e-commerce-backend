package com.agn1kobi.e_commerce_backend.order.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateOrderResponseDto(UUID orderId, BigDecimal totalPrice) {}
