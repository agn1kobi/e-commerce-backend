package com.agn1kobi.e_commerce_backend.product.api;

import com.agn1kobi.e_commerce_backend.product.dtos.CreateProductRequestDto;
import com.agn1kobi.e_commerce_backend.product.dtos.PaginatedResponseDto;
import com.agn1kobi.e_commerce_backend.product.dtos.ProductResponseDto;
import com.agn1kobi.e_commerce_backend.product.dtos.UpdateProductRequestDto;
import com.agn1kobi.e_commerce_backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	@GetMapping()
	public PaginatedResponseDto<ProductResponseDto> getAllProducts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		int safePage = Math.max(page, 0);
		int safeSize = Math.clamp(size, 1, 100);
		Pageable pageable = PageRequest.of(safePage, safeSize);
		return productService.getAllProducts(pageable);
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("uuid") UUID uuid) {
		Optional<ProductResponseDto> dto = productService.getProduct(uuid);
		return dto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PostMapping()
	public ResponseEntity<Void> createProduct(@Valid @RequestBody CreateProductRequestDto request) {

		boolean created = productService.createProduct(request);
		if (!created) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PatchMapping("/{uuid}")
	public ResponseEntity<Void> updateProduct(@PathVariable("uuid") UUID uuid, @Valid @RequestBody UpdateProductRequestDto request) {
		ProductService.UpdateResult result = productService.updateProduct(uuid, request);
		return switch (result) {
			case UPDATED -> ResponseEntity.noContent().build();
			case NOT_FOUND -> ResponseEntity.notFound().build();
			case CONFLICT -> ResponseEntity.status(HttpStatus.CONFLICT).build();
		};
	}
}
