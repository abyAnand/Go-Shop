package io.ecommerce.GoShop.Auth.Otp;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSmsSender {
    private final TwilioConfig twilioConfig;

    @Autowired
    public TwilioSmsSender(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }

    @Value("${twilio.phone.number}")
    String phoneNumber;

    public String formatPhoneNumber(String phoneNumber) {
        // Add "+91" prefix to the phone number
        return "+91" + phoneNumber;
    }

    public void sendSms(String toPhoneNumber, String otp) {
        com.twilio.Twilio.init(twilioConfig.getAccountSid(), twilioConfig.getAuthToken());

        String message = "Your OTP is: " + otp;

        String phoneNumber = formatPhoneNumber(toPhoneNumber);



        Message.creator(
                new PhoneNumber("+916282849733"),
                new PhoneNumber("+13614540810"),
                message
        ).create();
    }
}