package com.agn1kobi.e_commerce_backend.product.dtos;

import jakarta.validation.constraints.PositiveOrZero;

public record UpdateProductRequestDto(
        @PositiveOrZero Integer quantity,
        @PositiveOrZero Float price,
        @PositiveOrZero Float tax,
        String description
) {}
