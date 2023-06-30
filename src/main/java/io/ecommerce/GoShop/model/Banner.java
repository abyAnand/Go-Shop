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
public class Banner extends BaseEntity {

    private String title;

    private String description;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(mappedBy = "banner", cascade = CascadeType.ALL)
    @ToString.Exclude
    private BannerImage image;

}