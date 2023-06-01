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
public class Category extends BaseEntity{

    private String categoryName;

    @OneToMany(mappedBy = "category")
    @ToString.Exclude
    private List<Product> product;
}
