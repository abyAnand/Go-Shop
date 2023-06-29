package io.ecommerce.GoShop.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponResponse {
    private boolean valid;
    private boolean productSpecific;
    private boolean categorySpecific;
    private int discountPercentage;
    private boolean applicable;
}
