package io.ecommerce.GoShop.Auth.Otp;

import io.ecommerce.GoShop.service.user.UserService;
import io.ecommerce.GoShop.service.user.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    private final TwilioSmsSender twilioSmsSender;
    private final UserServiceInterface userService;
    private final ILoginService loginService;
    private final OtpService otpService;

    public SmsController(TwilioSmsSender twilioSmsSender, UserServiceInterface userService, ILoginService loginService, OtpService otpService) {
        this.twilioSmsSender = twilioSmsSender;
        this.userService = userService;
        this.loginService = loginService;
        this.otpService = otpService;
    }

    @PostMapping("/send-otp")
    public void sendSms(@RequestParam("phoneNumber") String toPhoneNumber, @RequestParam("sessionId") String sessionId) {
        String otp = OtpGenerator.generateOtp();
        System.out.println(otp);

        otpService.saveOtpWithSessionId(otp, sessionId);

        twilioSmsSender.sendSms(toPhoneNumber, otp);
    }

    @PostMapping("/login-otp")
    public void sendOtp(@RequestParam("username") String username) {
        String otp = OtpGenerator.generateOtp();
        System.out.println(otp);

        String phoneNumber = userService.findPhoneNumberByUsername(username);

        loginService.saveOtpWithusername(username, otp);

        twilioSmsSender.sendSms(phoneNumber, otp);
    }
}