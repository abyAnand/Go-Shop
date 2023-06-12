package io.ecommerce.GoShop.Auth.Otp;

import io.ecommerce.GoShop.model.LoginOtp;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

public interface OtpLoginRepository extends JpaRepository<LoginOtp, UUID> {
    Optional<LoginOtp> findByUsername(String username);

    @Transactional
    void deleteByUsername(String username);
}
