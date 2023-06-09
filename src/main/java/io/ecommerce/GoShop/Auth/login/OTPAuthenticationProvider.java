package io.ecommerce.GoShop.Auth.login;

import io.ecommerce.GoShop.Auth.Otp.OtpService;
import io.ecommerce.GoShop.model.OtpDto;
import io.ecommerce.GoShop.model.User;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OTPAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;

    public OTPAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    OtpService otpService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String otp = authentication.getCredentials().toString();
        List<GrantedAuthority> authorities;

        // Fetch the user's phone number from the database based on the user ID
        String phoneNumber = String.valueOf(userService.findPhonenUmberByUsername(username));

        // Verify the OTP
        boolean isOTPValid = verifyOTP(username, otp);

        if (!isOTPValid) {
            throw new BadCredentialsException("Invalid OTP");
        }

        // Fetch the user details from the database based on the user ID
        Optional<User> user = userService.findByUsername(username);

        authorities = Arrays.stream(user.get().getRole().getRoleName().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Create a fully authenticated authentication token
        return new UsernamePasswordAuthenticationToken(user, otp,authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private boolean verifyOTP(String username, String otp) {
        if (username == null || otp == null) {
            return false;
        }

        // Fetch the user's phone number from the database based on the user ID
        Optional<OtpDto> user = otpService.findBySessionId(username);


        // Verify the OTP
        if (user.get().getOtp().equals(otp)) {
            return true;
        }
        return false;
    }
}
