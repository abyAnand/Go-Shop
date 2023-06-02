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

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<Variant> variant;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
