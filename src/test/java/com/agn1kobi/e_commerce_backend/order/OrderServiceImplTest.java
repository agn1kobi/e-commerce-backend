package com.agn1kobi.e_commerce_backend.order;

import com.agn1kobi.e_commerce_backend.order.dtos.CreateOrderRequestDto;
import com.agn1kobi.e_commerce_backend.order.dtos.OrderItemRequestDto;
import com.agn1kobi.e_commerce_backend.order.event.OrderCreatedEvent;
import com.agn1kobi.e_commerce_backend.order.model.OrderEntity;
import com.agn1kobi.e_commerce_backend.order.repository.OrderLineRepository;
import com.agn1kobi.e_commerce_backend.order.repository.OrderRepository;
import com.agn1kobi.e_commerce_backend.order.service.OrderServiceImpl;
import com.agn1kobi.e_commerce_backend.product.service.ProductPricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    ProductPricingService pricing;

    @Mock
    OrderRepository orders;

    @Mock
    OrderLineRepository linesRepo;

    @Mock
    ApplicationEventPublisher publisher;

    OrderServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new OrderServiceImpl(pricing, orders, linesRepo, publisher);
    }


    @Test
    void createsOrder_whenValid() {
        UUID p1 = UUID.randomUUID();

        CreateOrderRequestDto req = new CreateOrderRequestDto(List.of(new OrderItemRequestDto(p1, 2)));

        when(pricing.getSnapshots(anyList())).thenReturn(Map.of(
                p1, new ProductPricingService.ProductSnapshot(p1, "Widget", 10.0f, 10.0f, 5)
        ));
        when(orders.save(any(OrderEntity.class))).thenAnswer(inv -> {
            OrderEntity e = inv.getArgument(0);
            e.setId(UUID.randomUUID());
            return e;
        });

        var outcome = service.createOrderWithResponse(req);

        assertThat(outcome.result()).isEqualTo(com.agn1kobi.e_commerce_backend.order.service.OrderService.CreateResult.CREATED);
        assertThat(outcome.body().orderId()).isNotNull();
        assertThat(outcome.body().totalPrice()).isEqualByComparingTo(new BigDecimal("22.0000")); // 2 * 10 * 1.1

        verify(linesRepo).saveAll(anyList());

        ArgumentCaptor<OrderCreatedEvent> captor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(publisher).publishEvent(captor.capture());
        OrderCreatedEvent evt = captor.getValue();
        assertThat(evt.lines()).hasSize(1);
        assertThat(evt.lines().get(0).productId()).isEqualTo(p1);
        assertThat(evt.lines().get(0).quantity()).isEqualTo(2);
    }

    @Test
    void failsWhenProductMissing() {
        UUID p1 = UUID.randomUUID();
        CreateOrderRequestDto req = new CreateOrderRequestDto(List.of(new OrderItemRequestDto(p1, 2)));
        when(pricing.getSnapshots(anyList())).thenReturn(Collections.emptyMap());

        var outcome = service.createOrderWithResponse(req);

        assertThat(outcome.result()).isEqualTo(com.agn1kobi.e_commerce_backend.order.service.OrderService.CreateResult.NOT_FOUND);
        verifyNoInteractions(orders, linesRepo, publisher);
    }

    @Test
    void failsWhenInsufficientQty() {
        UUID p1 = UUID.randomUUID();
        CreateOrderRequestDto req = new CreateOrderRequestDto(List.of(new OrderItemRequestDto(p1, 10)));
        when(pricing.getSnapshots(anyList())).thenReturn(Map.of(
                p1, new ProductPricingService.ProductSnapshot(p1, "Widget", 10.0f, 10.0f, 5)
        ));

        var outcome = service.createOrderWithResponse(req);

        assertThat(outcome.result()).isEqualTo(com.agn1kobi.e_commerce_backend.order.service.OrderService.CreateResult.INVALID_QUANTITY);
        verifyNoInteractions(orders, linesRepo, publisher);
    }
}
