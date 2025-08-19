package com.agn1kobi.e_commerce_backend.order.dtos;

import java.util.UUID;

public record OrderLineDto(UUID productId, int quantity, float lineTotal) {}
