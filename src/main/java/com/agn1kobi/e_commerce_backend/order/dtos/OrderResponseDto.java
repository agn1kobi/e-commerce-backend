package com.agn1kobi.e_commerce_backend.order.dtos;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


public record OrderResponseDto(
        UUID orderId,
        float totalPrice,
        OffsetDateTime createdAt,
        List<OrderLineDto> lines
) {}
