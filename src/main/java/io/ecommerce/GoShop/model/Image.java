package io.ecommerce.GoShop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Image extends BaseEntity {
    private String imagePath;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}