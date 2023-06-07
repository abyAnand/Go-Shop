package io.ecommerce.GoShop.Otp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SmsController {
    private final TwilioSmsSender twilioSmsSender;

    @Autowired
    public SmsController(TwilioSmsSender twilioSmsSender) {
        this.twilioSmsSender = twilioSmsSender;
    }

    @PostMapping("/send-otp")
    public void sendSms(@RequestParam("phoneNumber") String toPhoneNumber) {

        String otp = OtpGenerator.generateOtp();
        System.out.println(otp);
        twilioSmsSender.sendSms(toPhoneNumber, otp);
    }
}