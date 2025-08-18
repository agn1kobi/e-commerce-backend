package com.agn1kobi.e_commerce_backend.product.service;

import com.agn1kobi.e_commerce_backend.product.dtos.PaginatedResponseDto;
import com.agn1kobi.e_commerce_backend.product.dtos.ProductResponseDto;
import com.agn1kobi.e_commerce_backend.product.model.ProductEntity;
import com.agn1kobi.e_commerce_backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public PaginatedResponseDto<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<ProductEntity> page = productRepository.findAll(pageable);
        return new PaginatedResponseDto<>(
                page.stream()
                        .map(p -> new ProductResponseDto(p.getId(), p.getProductName(),p.getQuantity(), p.getPrice(), p.getTax(), p.getDescription()))
                        .collect(Collectors.toList()),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
