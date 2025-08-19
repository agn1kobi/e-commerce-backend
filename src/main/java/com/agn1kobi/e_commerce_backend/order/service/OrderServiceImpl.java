package com.agn1kobi.e_commerce_backend.order.service;

import com.agn1kobi.e_commerce_backend.common.types.RequestResult;
import com.agn1kobi.e_commerce_backend.common.types.Result;
import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderRequestDto;
import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderResponseDto;
import com.agn1kobi.e_commerce_backend.order.dtos.OrderRequestDto;
import com.agn1kobi.e_commerce_backend.order.dtos.OrderResponseDto;
import com.agn1kobi.e_commerce_backend.order.event.OrderCreatedEvent;
import com.agn1kobi.e_commerce_backend.order.dtos.OrderLineDto;
import com.agn1kobi.e_commerce_backend.order.model.OrderEntity;
import com.agn1kobi.e_commerce_backend.order.model.OrderLineEntity;
import com.agn1kobi.e_commerce_backend.order.repository.OrderLineRepository;
import com.agn1kobi.e_commerce_backend.order.repository.OrderRepository;
import com.agn1kobi.e_commerce_backend.product.service.ProductPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductPricingService productPricingService;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ApplicationEventPublisher eventPublisher;

    private float calculateTotal(float price, float tax, int quantity) {
        return price * tax * quantity;
    }

    @Override
    public RequestResult<CreateOrderResponseDto> createOrderWithResponse(CreateOrderRequestDto request) {
        Map<UUID, Integer> requested = new HashMap<>();
        for (OrderRequestDto item : request.items()) {
            requested.merge(item.productId(), item.quantity(), Integer::sum);
        }

        var snapshots = productPricingService.getSnapshots(new ArrayList<>(requested.keySet()));
        if (snapshots.size() != requested.size()) {
            return new RequestResult<>(Result.FAILURE, null);
        }

        for (Map.Entry<UUID, Integer> entry : requested.entrySet()) {
            UUID pid = entry.getKey();
            int want = entry.getValue();
            var snap = snapshots.get(pid);
            if (want <= 0 || snap == null || want > snap.quantity()) {
                return new RequestResult<>(Result.FAILURE, null);
            }
        }

        float totalPrice = 0f;
        for (Map.Entry<UUID, Integer> entry : requested.entrySet()) {
            UUID pid = entry.getKey();
            var snap = snapshots.get(pid);
            int want = entry.getValue();
            totalPrice += calculateTotal(snap.price(), snap.tax(), want);
        }

        OrderEntity saved = orderRepository.save(OrderEntity.builder().totalPrice(totalPrice).build());

        List<OrderLineEntity> lines = new ArrayList<>();
        for (Map.Entry<UUID, Integer> entry : requested.entrySet()) {
            UUID pid = entry.getKey();
            var snap = snapshots.get(pid);
            int want = entry.getValue();
            float lineTotal = calculateTotal(snap.price(), snap.tax(), want);
            lines.add(OrderLineEntity.builder()
                    .orderId(saved.getId())
                    .productId(pid)
                    .lineTotal(lineTotal)
                    .build());
        }
        orderLineRepository.saveAll(lines);

        var eventLines = lines.stream().map(l -> new OrderLineDto(l.getProductId(), l.getQuantity(), l.getLineTotal())).toList();
        eventPublisher.publishEvent(new OrderCreatedEvent(saved.getId(), eventLines));

        return new RequestResult<>(Result.SUCCESS, new CreateOrderResponseDto(saved.getId(), saved.getTotalPrice()));
    }

    @Override
    public RequestResult<OrderResponseDto> getOrderDetails(UUID orderId) {
        var maybeOrder = orderRepository.findById(orderId);
        if (maybeOrder.isEmpty()) return new RequestResult<>(Result.FAILURE, null);
        var order = maybeOrder.get();
        var lines = orderLineRepository.findByOrderId(orderId);
        var dtoLines = lines.stream()
                .map(l -> new OrderLineDto(l.getProductId(), l.getQuantity(), l.getLineTotal()))
                .toList();
        return new RequestResult<>(Result.SUCCESS, new OrderResponseDto(order.getId(), order.getTotalPrice(), order.getCreatedAt(), dtoLines));
    }
}
