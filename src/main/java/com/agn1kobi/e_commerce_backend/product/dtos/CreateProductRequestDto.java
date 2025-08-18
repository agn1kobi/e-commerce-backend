package com.agn1kobi.e_commerce_backend.product.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public record CreateProductRequestDto(
        @NotBlank String productName,
        @PositiveOrZero int quantity,
        @PositiveOrZero float price,
        @PositiveOrZero float tax,
        String description
) {}
