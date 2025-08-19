package com.agn1kobi.e_commerce_backend.product.service;

import com.agn1kobi.e_commerce_backend.product.dtos.ProductSnapshotDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface ProductPricingService {

    Optional<ProductSnapshotDto> getSnapshot(UUID productId);

    Map<UUID, ProductSnapshotDto> getSnapshots(List<UUID> productIds);
}
