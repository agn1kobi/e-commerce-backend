package com.agn1kobi.e_commerce_backend.product.service;

import com.agn1kobi.e_commerce_backend.common.types.Result;
import com.agn1kobi.e_commerce_backend.product.dtos.CreateProductRequestDto;
import com.agn1kobi.e_commerce_backend.product.dtos.PaginatedResponseDto;
import com.agn1kobi.e_commerce_backend.product.dtos.ProductResponseDto;
import com.agn1kobi.e_commerce_backend.product.dtos.UpdateProductRequestDto;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

public interface ProductService {
    PaginatedResponseDto<ProductResponseDto> getAllProducts(Pageable pageable);

    Optional<ProductResponseDto> getProduct(UUID id);

    Result createProduct(CreateProductRequestDto request);
    
    Result updateProduct(UUID id, UpdateProductRequestDto request);
}
