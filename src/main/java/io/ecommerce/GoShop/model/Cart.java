package io.ecommerce.GoShop.model;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseEntity{


    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;


    @OneToMany(mappedBy = "cart")
    @ToString.Exclude
    List<CartItem> cartItems = new ArrayList<CartItem>();

}
