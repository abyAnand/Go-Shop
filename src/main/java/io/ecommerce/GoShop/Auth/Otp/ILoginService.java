package io.ecommerce.GoShop.Auth.Otp;


import io.ecommerce.GoShop.model.LoginOtp;


import java.util.Optional;

public interface ILoginService {
    void saveOtpWithusername(String username, String otp);
    Optional<LoginOtp> findByusername(String username);
}
