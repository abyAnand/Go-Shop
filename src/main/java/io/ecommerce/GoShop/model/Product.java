package io.ecommerce.GoShop.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Product extends BaseEntity{

    private String productName;


    private float price;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Image> images;

    @Lob
    private String shortDescription;

    @Lob
    private String longDescription;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<Variant> variant;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany
    @JoinColumn(name = "coupon_id")
    @ToString.Exclude
    private List<Coupon> coupon;

}
