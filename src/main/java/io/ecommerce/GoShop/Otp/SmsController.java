package io.ecommerce.GoShop.Otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {

    @Autowired
    TwilioSmsSender twilioSmsSender;

    @Autowired
    OtpService otpService;



    @PostMapping("/send-otp")
    public void sendSms(@RequestParam("phoneNumber") String toPhoneNumber, @RequestParam("sessionId") String sessionId) {
        String otp = OtpGenerator.generateOtp();
        System.out.println(otp);

        otpService.saveOtpWithSessionId(otp, sessionId);

        twilioSmsSender.sendSms(toPhoneNumber, otp);
    }
}