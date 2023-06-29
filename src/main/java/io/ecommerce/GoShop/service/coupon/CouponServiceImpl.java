package io.ecommerce.GoShop.service.coupon;

import io.ecommerce.GoShop.model.Coupon;
import io.ecommerce.GoShop.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CouponServiceImpl implements CouponService{

    @Autowired
    CouponRepository couponRepository;
    @Override
    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }


    @Override
    public void deleteCoupon(Coupon coupon) {
        couponRepository.delete(coupon);
    }

    @Override
    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    @Override
    public Optional<Coupon> findById(UUID uuid) {
        return couponRepository.findById(uuid);
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return couponRepository.findByCode(code);
    }
}
