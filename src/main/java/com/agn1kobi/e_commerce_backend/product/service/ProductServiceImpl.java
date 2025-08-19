package com.agn1kobi.e_commerce_backend.product.service;

import com.agn1kobi.e_commerce_backend.common.types.Result;
import com.agn1kobi.e_commerce_backend.product.dtos.PaginatedResponseDto;
import com.agn1kobi.e_commerce_backend.product.dtos.CreateProductRequestDto;
import com.agn1kobi.e_commerce_backend.product.dtos.ProductResponseDto;
import com.agn1kobi.e_commerce_backend.product.dtos.UpdateProductRequestDto;
import com.agn1kobi.e_commerce_backend.product.model.ProductEntity;
import com.agn1kobi.e_commerce_backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public PaginatedResponseDto<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<ProductEntity> page = productRepository.findAll(pageable);
        return new PaginatedResponseDto<>(
        page.stream()
            .map(p -> new ProductResponseDto(p.getId(), p.getProductName(), p.getQuantity(), p.getPrice(), p.getTax(), p.getDescription()))
            .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public Optional<ProductResponseDto> getProduct(UUID id) {
        return productRepository.findById(id)
                .map(p -> new ProductResponseDto(p.getId(), p.getProductName(), p.getQuantity(), p.getPrice(), p.getTax(), p.getDescription()));
    }

    @Override
    public Result createProduct(CreateProductRequestDto request) {
        if (productRepository.existsByProductName(request.productName())) {
            return Result.FAILURE;
        }

        ProductEntity entity = ProductEntity.builder()
                .productName(request.productName())
                .quantity(request.quantity())
                .price(request.price())
                .tax(request.tax())
                .description(request.description())
                .build();
        productRepository.save(entity);
        return Result.SUCCESS;
    }

    @Override
    public Result updateProduct(UUID id, UpdateProductRequestDto request) {
        Optional<ProductEntity> maybe = productRepository.findById(id);

        if (maybe.isEmpty()) return Result.FAILURE;
        ProductEntity entity = maybe.get();

        applyUpdate(request, entity);
        productRepository.save(entity);

        return Result.SUCCESS;
    }


    private void applyUpdate(UpdateProductRequestDto request, ProductEntity entity) {
        if (request.quantity() != null) entity.setQuantity(request.quantity());
        if (request.price() != null) entity.setPrice(request.price());
        if (request.tax() != null) entity.setTax(request.tax());
        if (request.description() != null) entity.setDescription(request.description());
    }
}
