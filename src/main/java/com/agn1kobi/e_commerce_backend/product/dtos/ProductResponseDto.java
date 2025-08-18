package com.agn1kobi.e_commerce_backend.product.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductResponseDto {
    private UUID productId;
    private String productName;
    private int quantity;
    private float price;
    private float tax;
    private String description;

}

