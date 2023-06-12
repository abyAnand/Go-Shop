package io.ecommerce.GoShop.Auth.login;

import io.ecommerce.GoShop.Auth.Otp.OtpGenerator;
import io.ecommerce.GoShop.Auth.Otp.OtpService;
import io.ecommerce.GoShop.Auth.Otp.TwilioSmsSender;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class OTPAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final OtpService otpService;

    public OTPAuthenticationFilter(AuthenticationManager authenticationManager, OtpService otpService) {
        super.setAuthenticationManager(authenticationManager);
        this.otpService = otpService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String otp = obtainOtp(request);
        String username = obtainUsername(request);

        if (otp == null || username == null) {
            throw new AuthenticationServiceException("OTP and username must be provided");
        }

        otp = otp.trim();
        username = username.trim();

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, otp);

        return this.getAuthenticationManager().authenticate(authentication);
    }

    private String obtainOtp(HttpServletRequest request) {
        return request.getParameter("otp");
    }

    protected String obtainUsername(HttpServletRequest request) {
        return request.getParameter("username");
    }
}





