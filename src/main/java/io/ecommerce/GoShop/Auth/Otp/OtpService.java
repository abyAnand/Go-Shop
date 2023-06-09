package io.ecommerce.GoShop.Auth.Otp;

import io.ecommerce.GoShop.model.OtpDto;

import java.util.Optional;

public interface OtpService {

    void saveOtpWithSessionId(String otp, String sessionId);

    Optional<OtpDto> findBySessionId(String sessionId);
}
