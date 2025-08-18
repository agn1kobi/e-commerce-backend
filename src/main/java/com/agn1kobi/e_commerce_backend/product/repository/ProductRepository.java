package com.agn1kobi.e_commerce_backend.product.repository;

import com.agn1kobi.e_commerce_backend.product.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
