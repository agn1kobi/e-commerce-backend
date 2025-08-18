package com.agn1kobi.e_commerce_backend.order.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDetailsResponseDto(
        UUID orderId,
        BigDecimal totalPrice,
        OffsetDateTime createdAt,
        List<Line> lines
) {
    public record Line(UUID productId, String productName, int quantity, BigDecimal unitPrice, BigDecimal taxPct, BigDecimal lineTotal) {}
}
