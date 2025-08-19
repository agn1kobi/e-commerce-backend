package com.agn1kobi.e_commerce_backend.order.event;

import com.agn1kobi.e_commerce_backend.order.dtos.OrderLineDto;

import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(UUID orderId, List<OrderLineDto> lines) {
}