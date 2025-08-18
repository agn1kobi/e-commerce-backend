package com.agn1kobi.e_commerce_backend.order.event;

import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(UUID orderId, List<OrderLine> lines) {
	public record OrderLine(UUID productId, int quantity) {}
}