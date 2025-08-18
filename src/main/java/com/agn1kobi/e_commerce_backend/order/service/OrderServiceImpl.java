package com.agn1kobi.e_commerce_backend.order.service;

import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderRequestDto;
import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderResponseDto;
import com.agn1kobi.e_commerce_backend.order.dtos.OrderItemRequestDto;
import com.agn1kobi.e_commerce_backend.order.dtos.OrderDetailsResponseDto;
import com.agn1kobi.e_commerce_backend.order.event.OrderCreatedEvent;
import com.agn1kobi.e_commerce_backend.order.model.OrderEntity;
import com.agn1kobi.e_commerce_backend.order.model.OrderLineEntity;
import com.agn1kobi.e_commerce_backend.order.repository.OrderLineRepository;
import com.agn1kobi.e_commerce_backend.order.repository.OrderRepository;
import com.agn1kobi.e_commerce_backend.product.service.ProductPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final ProductPricingService productPricingService;
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CreateResult createOrder(CreateOrderRequestDto request) {
        // Validate products exist and quantities
        Map<UUID, Integer> requested = new HashMap<>();
        for (OrderItemRequestDto item : request.items()) {
            requested.merge(item.productId(), item.quantity(), Integer::sum);
        }

        var snapshots = productPricingService.getSnapshots(new ArrayList<>(requested.keySet()));
        if (snapshots.size() != requested.size()) {
            return CreateResult.NOT_FOUND;
        }

        for (Map.Entry<UUID, Integer> entry : requested.entrySet()) {
            UUID pid = entry.getKey();
            int want = entry.getValue();
            var snap = snapshots.get(pid);
            if (want <= 0 || snap == null || want > snap.availableQty()) {
                return CreateResult.INVALID_QUANTITY;
            }
        }

        // Calculate total price = sum(quantity * price * (1 + tax/100))
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<UUID, Integer> entry : requested.entrySet()) {
            UUID pid = entry.getKey();
            var snap = snapshots.get(pid);
            int want = entry.getValue();
            BigDecimal price = BigDecimal.valueOf(snap.unitPrice());
            BigDecimal taxPct = BigDecimal.valueOf(snap.taxPct()).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            BigDecimal line = price.multiply(BigDecimal.ONE.add(taxPct)).multiply(BigDecimal.valueOf(want));
            total = total.add(line);
        }
        // Persist order
        var saved = orderRepository.save(OrderEntity.builder().totalPrice(total.setScale(4, RoundingMode.HALF_UP)).build());
        // Persist lines
        List<OrderLineEntity> lines = new ArrayList<>();
        for (Map.Entry<UUID, Integer> entry : requested.entrySet()) {
            UUID pid = entry.getKey();
            var snap = snapshots.get(pid);
            int want = entry.getValue();
            BigDecimal unitPrice = BigDecimal.valueOf(snap.unitPrice());
            BigDecimal taxPct = BigDecimal.valueOf(snap.taxPct()).setScale(4, RoundingMode.HALF_UP);
            BigDecimal taxFactor = taxPct.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.ONE.add(taxFactor)).multiply(BigDecimal.valueOf(want)).setScale(4, RoundingMode.HALF_UP);
            lines.add(OrderLineEntity.builder()
                    .orderId(saved.getId())
                    .productId(pid)
                    .productName(snap.productName())
                    .quantity(want)
                    .unitPrice(unitPrice.setScale(4, RoundingMode.HALF_UP))
                    .taxPct(taxPct)
                    .lineTotal(lineTotal)
                    .build());
        }
        orderLineRepository.saveAll(lines);

        // Publish event for stock deduction
    var eventLines = lines.stream().map(l -> new OrderCreatedEvent.OrderLine(l.getProductId(), l.getQuantity())).toList();
    eventPublisher.publishEvent(new OrderCreatedEvent(saved.getId(), eventLines));

        return CreateResult.CREATED;
    }

    @Override
    public CreateOutcome createOrderWithResponse(CreateOrderRequestDto request) {
        // Reuse logic but return details
        Map<UUID, Integer> requested = new HashMap<>();
        for (OrderItemRequestDto item : request.items()) {
            requested.merge(item.productId(), item.quantity(), Integer::sum);
        }

        var snapshots = productPricingService.getSnapshots(new ArrayList<>(requested.keySet()));
        if (snapshots.size() != requested.size()) {
            return new CreateOutcome(CreateResult.NOT_FOUND, null);
        }

        for (Map.Entry<UUID, Integer> entry : requested.entrySet()) {
            UUID pid = entry.getKey();
            int want = entry.getValue();
            var snap = snapshots.get(pid);
            if (want <= 0 || snap == null || want > snap.availableQty()) {
                return new CreateOutcome(CreateResult.INVALID_QUANTITY, null);
            }
        }

        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<UUID, Integer> entry : requested.entrySet()) {
            UUID pid = entry.getKey();
            var snap = snapshots.get(pid);
            int want = entry.getValue();
            BigDecimal price = BigDecimal.valueOf(snap.unitPrice());
            BigDecimal taxPct = BigDecimal.valueOf(snap.taxPct()).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            BigDecimal line = price.multiply(BigDecimal.ONE.add(taxPct)).multiply(BigDecimal.valueOf(want));
            total = total.add(line);
        }

        OrderEntity saved = orderRepository.save(OrderEntity.builder().totalPrice(total.setScale(4, RoundingMode.HALF_UP)).build());

        List<OrderLineEntity> lines = new ArrayList<>();
        for (Map.Entry<UUID, Integer> entry : requested.entrySet()) {
            UUID pid = entry.getKey();
            var snap = snapshots.get(pid);
            int want = entry.getValue();
            BigDecimal unitPrice = BigDecimal.valueOf(snap.unitPrice());
            BigDecimal taxPct = BigDecimal.valueOf(snap.taxPct()).setScale(4, RoundingMode.HALF_UP);
            BigDecimal taxFactor = taxPct.divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.ONE.add(taxFactor)).multiply(BigDecimal.valueOf(want)).setScale(4, RoundingMode.HALF_UP);
            lines.add(OrderLineEntity.builder()
                    .orderId(saved.getId())
                    .productId(pid)
                    .productName(snap.productName())
                    .quantity(want)
                    .unitPrice(unitPrice.setScale(4, RoundingMode.HALF_UP))
                    .taxPct(taxPct)
                    .lineTotal(lineTotal)
                    .build());
        }
        orderLineRepository.saveAll(lines);

        var eventLines = lines.stream().map(l -> new OrderCreatedEvent.OrderLine(l.getProductId(), l.getQuantity())).toList();
        eventPublisher.publishEvent(new OrderCreatedEvent(saved.getId(), eventLines));

        return new CreateOutcome(CreateResult.CREATED, new CreateOrderResponseDto(saved.getId(), saved.getTotalPrice()));
    }

    @Override
    public java.util.Optional<OrderDetailsResponseDto> getOrderDetails(java.util.UUID orderId) {
        var maybeOrder = orderRepository.findById(orderId);
        if (maybeOrder.isEmpty()) return java.util.Optional.empty();
        var order = maybeOrder.get();
        var lines = orderLineRepository.findByOrderId(orderId);
        var dtoLines = lines.stream()
                .map(l -> new OrderDetailsResponseDto.Line(l.getProductId(), l.getProductName(), l.getQuantity(), l.getUnitPrice(), l.getTaxPct(), l.getLineTotal()))
                .toList();
        return java.util.Optional.of(new OrderDetailsResponseDto(order.getId(), order.getTotalPrice(), order.getCreatedAt(), dtoLines));
    }
}
