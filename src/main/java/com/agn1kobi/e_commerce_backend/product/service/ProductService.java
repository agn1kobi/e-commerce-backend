package com.agn1kobi.e_commerce_backend.product.service;

import com.agn1kobi.e_commerce_backend.product.dtos.PaginatedResponseDto;
import com.agn1kobi.e_commerce_backend.product.dtos.ProductResponseDto;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    PaginatedResponseDto<ProductResponseDto> getAllProducts(Pageable pageable);
}
