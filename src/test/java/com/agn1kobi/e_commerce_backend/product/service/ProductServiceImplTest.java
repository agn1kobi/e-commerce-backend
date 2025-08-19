package com.agn1kobi.e_commerce_backend.product.service;

import com.agn1kobi.e_commerce_backend.common.types.Result;
import com.agn1kobi.e_commerce_backend.product.dtos.CreateProductRequestDto;
import com.agn1kobi.e_commerce_backend.product.dtos.UpdateProductRequestDto;
import com.agn1kobi.e_commerce_backend.product.model.ProductEntity;
import com.agn1kobi.e_commerce_backend.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository repo;

    ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ProductServiceImpl(repo);
    }


    @Test
    void createProduct_conflictOnDuplicateName() {
        when(repo.existsByProductName("Widget")).thenReturn(true);
        Result created = service.createProduct(new CreateProductRequestDto("Widget", 1, 10.0f, 5.0f, null));
        assertThat(created).isEqualTo(Result.FAILURE);
        verify(repo, never()).save(any());
    }

    @Test
    void createProduct_success() {
        when(repo.existsByProductName("Widget")).thenReturn(false);
        Result created = service.createProduct(new CreateProductRequestDto("Widget", 5, 10.0f, 5.0f, "desc"));
        assertThat(created).isEqualTo(Result.SUCCESS);
        verify(repo).save(any(ProductEntity.class));
    }

    @Test
    void updateProduct_notFound() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());
        var result = service.updateProduct(id, new UpdateProductRequestDto(null, null, null, null));
        assertThat(result).isEqualTo(Result.FAILURE);
    }

    @Test
    void updateProduct_updated() {
        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.of(ProductEntity.builder().id(id).quantity(1).price(1.0f).tax(0f).build()));
        var result = service.updateProduct(id, new UpdateProductRequestDto(10, 20.0f, 15.0f, "new"));
        assertThat(result).isEqualTo(Result.SUCCESS);
        verify(repo).save(any(ProductEntity.class));
    }
}
