package io.ecommerce.GoShop.Otp;

import io.ecommerce.GoShop.model.OtpDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OtpServiceImpl implements OtpService {

    @Autowired
    OtpRepository otpRepository;
    @Override
    public void saveOtpWithSessionId(String otp, String sessionId) {


        OtpDto otpDto   = new OtpDto();
        otpDto.setOtp(otp);
        otpDto.setSessionId(sessionId);
        otpRepository.save(otpDto);
    }

    @Override
    public Optional<OtpDto> findBySessionId(String sessionId) {
        return otpRepository.findBySessionId(sessionId);
    }
}
