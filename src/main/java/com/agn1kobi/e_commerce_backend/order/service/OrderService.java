package com.agn1kobi.e_commerce_backend.order.service;

import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderRequestDto;
import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderResponseDto;
import com.agn1kobi.e_commerce_backend.order.dtos.OrderDetailsResponseDto;

public interface OrderService {
    enum CreateResult { CREATED, NOT_FOUND, INVALID_QUANTITY }
    CreateResult createOrder(CreateOrderRequestDto request);

    /**
     * Alternative API: create and return details
     */
    record CreateOutcome(CreateResult result, CreateOrderResponseDto body) {}

    CreateOutcome createOrderWithResponse(CreateOrderRequestDto request);

    java.util.Optional<OrderDetailsResponseDto> getOrderDetails(java.util.UUID orderId);
}
