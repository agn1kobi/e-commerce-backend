package com.agn1kobi.e_commerce_backend.order.service;

import com.agn1kobi.e_commerce_backend.common.types.RequestResult;
import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderRequestDto;
import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderResponseDto;
import com.agn1kobi.e_commerce_backend.order.dtos.OrderResponseDto;

import java.util.UUID;


public interface OrderService {


    RequestResult<CreateOrderResponseDto> createOrderWithResponse(CreateOrderRequestDto request);

    RequestResult<OrderResponseDto> getOrderDetails(java.util.UUID orderId);
}
