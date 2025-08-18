package com.agn1kobi.e_commerce_backend.product.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products",  uniqueConstraints = {@UniqueConstraint(name = "uk_on_product_name", columnNames = "product_name")})
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String productName;

    @Column(nullable = false)
    @PositiveOrZero
    private int quantity;

    @Column(nullable = false)
    @PositiveOrZero
    private float price;

    @Column(nullable = false)
    @PositiveOrZero
    private float tax;

    @Column()
    private String description;
}
