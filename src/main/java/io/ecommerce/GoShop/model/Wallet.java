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
public class Wallet extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "user_id")
    User user;

    double balance;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL)
    @ToString.Exclude
    List<WalletHistory> history = new ArrayList<>();
}
