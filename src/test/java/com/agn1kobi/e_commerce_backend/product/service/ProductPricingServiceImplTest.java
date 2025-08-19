package com.agn1kobi.e_commerce_backend.product.service;


import com.agn1kobi.e_commerce_backend.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ProductPricingServiceImplTest {

    @Mock
    ProductRepository productRepository;

    ProductPricingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductPricingServiceImpl(productRepository);
    }

    @Test
    void getSnapshot() {
        UUID productId = UUID.randomUUID();

        service.getSnapshot(productId);

        verify(productRepository).findById(productId);

    }

    @Test
    void getSnapshots() {

    }
}
