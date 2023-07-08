package io.ecommerce.GoShop.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;


@Entity
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Coupon extends BaseEntity{

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Coupon code is required")
    private String code;
    private int discount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expirationPeriod;
    private int couponStock;
    private int maximumDiscountAmount;
    private CouponType type;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean isDeleted;
}
