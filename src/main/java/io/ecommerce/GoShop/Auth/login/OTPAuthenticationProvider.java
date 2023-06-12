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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OTPAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final OtpService otpService;
    private final UserService userService;

    public OTPAuthenticationProvider(UserDetailsService userDetailsService, UserService userService, OtpService otpService) {
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.otpService = otpService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String otp = authentication.getCredentials() != null ? authentication.getCredentials().toString() : null;
        String username = authentication.getName();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (otp != null && userDetails != null && userDetails.getPassword().equals(otp)) {
            return new UsernamePasswordAuthenticationToken(userDetails, otp, userDetails.getAuthorities());
        }

        throw new BadCredentialsException("Invalid OTP");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
