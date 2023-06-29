package io.ecommerce.GoShop.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
public class Order extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "address_id")
    Address address;

    Payment payment;

    Status status;

    float total;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    Coupon coupon;

    public int getSize(){
        return orderItems.size();
    }




}
