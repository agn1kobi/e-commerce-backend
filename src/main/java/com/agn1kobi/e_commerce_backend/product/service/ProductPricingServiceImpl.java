package com.agn1kobi.e_commerce_backend.product.service;

import com.agn1kobi.e_commerce_backend.product.dtos.ProductSnapshotDto;
import com.agn1kobi.e_commerce_backend.product.model.ProductEntity;
import com.agn1kobi.e_commerce_backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductPricingServiceImpl implements ProductPricingService {

    private final ProductRepository productRepository;

    @Override
    public Optional<ProductSnapshotDto> getSnapshot(UUID productId) {
        return productRepository.findById(productId)
                .map(p -> new ProductSnapshotDto(p.getId(), p.getProductName(), p.getPrice(), p.getTax(), p.getQuantity()));
    }

    @Override
    public Map<UUID, ProductSnapshotDto> getSnapshots(List<UUID> productIds) {
        if (productIds == null || productIds.isEmpty()) return Collections.emptyMap();
        List<ProductEntity> products = productRepository.findAllById(productIds);
        Map<UUID, ProductSnapshotDto> map = new HashMap<>();
        for (ProductEntity p : products) {
            map.put(p.getId(), new ProductSnapshotDto(p.getId(), p.getProductName(), p.getPrice(), p.getTax(), p.getQuantity()));
        }
        return map;
    }
}
