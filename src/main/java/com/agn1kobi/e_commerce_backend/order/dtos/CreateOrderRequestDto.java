package com.agn1kobi.e_commerce_backend.order.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateOrderRequestDto(
        @NotEmpty List<@Valid OrderRequestDto> items
) {}
