package io.ecommerce.GoShop.service.coupon;

import io.ecommerce.GoShop.model.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CouponService {

    Coupon save(Coupon coupon);



    void deleteCoupon(Coupon coupon);

    List<Coupon> findAll();

    Optional<Coupon> findById(UUID uuid);

    Optional<Coupon> findByCode(String code);


    Page<Coupon> findAllPaged(Pageable pageable);

    Page<Coupon> findByCodeLike(Pageable pageable, String keyword);
}
