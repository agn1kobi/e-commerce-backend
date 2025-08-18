package com.agn1kobi.e_commerce_backend.order.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record OrderItemRequestDto(
        @NotNull UUID productId,
        @Min(1) int quantity
) {}
