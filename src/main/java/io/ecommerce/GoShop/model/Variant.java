package io.ecommerce.GoShop.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Variant extends BaseEntity{

    private String variantName;

    private int stock;

    private float price;

    private String description;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
