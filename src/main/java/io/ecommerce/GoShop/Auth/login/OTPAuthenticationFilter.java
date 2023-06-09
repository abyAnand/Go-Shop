package io.ecommerce.GoShop.Auth.login;

import io.ecommerce.GoShop.Auth.Otp.OtpGenerator;
import io.ecommerce.GoShop.Auth.Otp.OtpService;
import io.ecommerce.GoShop.Auth.Otp.TwilioSmsSender;
import io.ecommerce.GoShop.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class OTPAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    UserService userService;

    @Autowired
    private TwilioSmsSender twilioSmsSender;

    @Autowired
    private OtpService otpService;

    public OTPAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        // Retrieve the user ID from the request
        String username = request.getParameter("username");

        // Fetch the user's phone number from the database based on the user ID
        String phoneNumber = String.valueOf(userService.findPhonenUmberByUsername(username));

        // Generate an OTP
        String otp = generateOTP();
        otpService.saveOtpWithSessionId(otp, username);

        // Send the OTP to the user's phone number
        sendOTP(phoneNumber, otp);

        // Create an authentication token with the OTP
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, otp);

        // Authenticate the token
        return getAuthenticationManager().authenticate(authentication);
    }

    private String generateOTP() {
        return OtpGenerator.generateOtp();
    }

    private void sendOTP(String phoneNumber, String otp) {
        twilioSmsSender.sendSms(phoneNumber, otp);
    }
}





