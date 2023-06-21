package io.ecommerce.GoShop.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "variant_id")
    private Variant variant;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private float price;
}
