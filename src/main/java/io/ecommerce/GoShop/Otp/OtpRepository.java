package io.ecommerce.GoShop.Otp;

import io.ecommerce.GoShop.model.OtpDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpDto, String> {

    Optional<OtpDto> findBySessionId(String sessionId);

    void deleteBySessionId(String sessionId);


    void deleteByCreatedDateLessThan(LocalDateTime thresholdTime);
}
