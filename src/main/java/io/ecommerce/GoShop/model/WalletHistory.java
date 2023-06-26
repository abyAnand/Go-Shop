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
public class WalletHistory extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    private String amount;

    private Transaction transaction;

}
