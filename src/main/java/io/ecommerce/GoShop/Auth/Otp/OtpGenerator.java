package io.ecommerce.GoShop.Auth.Otp;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpGenerator {

    public static String generateOtp() {
        // Generate the OTP
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            otpBuilder.append(digit);
        }

        return otpBuilder.toString();
    }

}