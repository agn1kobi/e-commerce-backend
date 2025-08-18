package com.agn1kobi.e_commerce_backend.product.api;

import com.agn1kobi.e_commerce_backend.product.dtos.PaginatedResponseDto;
import com.agn1kobi.e_commerce_backend.product.dtos.ProductResponseDto;
import com.agn1kobi.e_commerce_backend.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public PaginatedResponseDto<ProductResponseDto> getAllProducts(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size
	) {
		int safePage = Math.max(page, 0);
		int safeSize = Math.clamp(size, 1, 100);
		Pageable pageable = PageRequest.of(safePage, safeSize);
		return productService.getAllProducts(pageable);
	}
}
