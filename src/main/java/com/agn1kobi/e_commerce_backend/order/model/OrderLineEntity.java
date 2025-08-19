package com.agn1kobi.e_commerce_backend.order.model;

import com.agn1kobi.e_commerce_backend.common.model.Auditable;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_lines")
public class OrderLineEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column( nullable = false)
    private int quantity;

    @Column(name = "line_total", nullable = false)
    private float lineTotal;
}
