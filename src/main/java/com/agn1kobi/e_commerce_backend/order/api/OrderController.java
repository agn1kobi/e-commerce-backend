package com.agn1kobi.e_commerce_backend.order.api;

import com.agn1kobi.e_commerce_backend.common.types.RequestResult;
import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderRequestDto;
import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderResponseDto;
import com.agn1kobi.e_commerce_backend.order.dtos.OrderResponseDto;
import com.agn1kobi.e_commerce_backend.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CreateOrderResponseDto> createOrder(@Valid @RequestBody CreateOrderRequestDto request) {
        RequestResult<CreateOrderResponseDto> result = orderService.createOrderWithResponse(request);

        return switch (result.result()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.CREATED).body(result.response());
            case FAILURE -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        };
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable("orderId") java.util.UUID orderId) {
        RequestResult<OrderResponseDto> result = orderService.getOrderDetails(orderId);

        return switch (result.result()) {
            case SUCCESS -> ResponseEntity.status(HttpStatus.OK).body(result.response());
            case FAILURE -> ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        };
    }
}
