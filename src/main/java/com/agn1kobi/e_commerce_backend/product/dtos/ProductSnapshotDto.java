package com.agn1kobi.e_commerce_backend.product.dtos;

import java.util.UUID;

public record ProductSnapshotDto(UUID productId, String productName, float price, float tax, int quantity) {}

