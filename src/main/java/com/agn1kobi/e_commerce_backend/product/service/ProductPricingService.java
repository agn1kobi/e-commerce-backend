package com.agn1kobi.e_commerce_backend.product.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ProductPricingService {
    record ProductSnapshot(UUID productId, String productName, float unitPrice, float taxPct, int availableQty) {}

    Optional<ProductSnapshot> getSnapshot(UUID productId);

    Map<UUID, ProductSnapshot> getSnapshots(List<UUID> productIds);
}
