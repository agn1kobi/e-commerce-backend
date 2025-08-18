package com.agn1kobi.e_commerce_backend.product.listener;

import com.agn1kobi.e_commerce_backend.order.event.OrderCreatedEvent;
import com.agn1kobi.e_commerce_backend.product.model.ProductEntity;
import com.agn1kobi.e_commerce_backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductInventoryListener {

    private final ProductRepository productRepository;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional
    public void onOrderCreated(final OrderCreatedEvent event) {
        for (OrderCreatedEvent.OrderLine line : event.lines()) {
            Optional<ProductEntity> maybe = productRepository.findById(line.productId());
            if (maybe.isEmpty()) continue;
            ProductEntity product = maybe.get();
            int remaining = product.getQuantity() - line.quantity();
            if (remaining < 0) remaining = 0;
            product.setQuantity(remaining);
            productRepository.save(product);
        }
    }
}
