package io.ecommerce.GoShop.repository;

import io.ecommerce.GoShop.model.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, UUID> {
    Optional<Coupon> findByCode(String code);




    Page<Coupon> findByCodeLike(String s,
                                Pageable pageable);
}
